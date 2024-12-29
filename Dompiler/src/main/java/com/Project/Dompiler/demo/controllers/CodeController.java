package com.Project.Dompiler.demo.controllers;

import com.Project.Dompiler.demo.DTO.CodeRequest;
import com.Project.Dompiler.demo.DTO.CodeRequestToServer;
import com.Project.Dompiler.demo.DTO.CodeResponse;
import com.Project.Dompiler.demo.beans.Code;
import com.Project.Dompiler.demo.beans.Profile;
import com.Project.Dompiler.demo.beans.User;
import com.Project.Dompiler.demo.service.CodeService;
import com.Project.Dompiler.demo.service.LoginService;
import com.Project.Dompiler.demo.service.ProfileService;
import com.Project.Dompiler.demo.utils.ConnectionPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class CodeController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private LoginService userLoginService;

    @Autowired
    private CodeService codeService;

    @GetMapping("/codes/{userId}")
    public ResponseEntity<?> getCodesByUser(@PathVariable String userId) {
        User user = userLoginService.findByUserId(userId);
        List<Code> rlist = codeService.getCodesByProfileId(user.getProfile());
        if (rlist.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(rlist);
    }

    @PostMapping("/compile/{userId}")
    public void compileCode(@PathVariable String userId, @RequestBody CodeRequest codeRequest) {
        User user = userLoginService.findByUserId(userId);
        Profile profile = user.getProfile();
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        Code code = new Code(uuid, codeRequest.getCode());
        ConnectionPoolService connectionPoolService = ConnectionPoolService.create();

        WebClient webClient = connectionPoolService.getConnection();
        webClient.post()
                .uri("/api/v1/compile")
                .bodyValue(BodyInserters.fromValue(code))
                .retrieve()
                .toEntity(CodeResponse.class)   //Change here
                .subscribe(
                        responseEntity -> {
                            // Handle success response here
                            HttpStatusCode status = responseEntity.getStatusCode();
                            URI location = responseEntity.getHeaders().getLocation();
                            CodeResponse createdEmployee = responseEntity.getBody();    // Response body
                            // handle response as necessary
                            code.setCompiledSuccess(createdEmployee.getStatus());
                            if (!createdEmployee.getStatus().equals("Success")) {
                                code.setErrorMessage(createdEmployee.getCodeResString());
                            }
                            codeService.saveCode(code);
                        },
                        error -> {
                            // Handle the error here
                            if (error instanceof WebClientResponseException) {
                                WebClientResponseException ex = (WebClientResponseException) error;
                                HttpStatusCode status = ex.getStatusCode();
                                System.out.println("Error Status Code: " + status.value());
                                //...
                            } else {
                                // Handle other types of errors
                                System.err.println("An unexpected error occurred: " + error.getMessage());
                            }
                        }
                );
    }

    @PostMapping("/run/{userId}")
    public void runCode(@PathVariable String userId, @RequestBody CodeRequest codeRequest) {
        User user = userLoginService.findByUserId(userId);
        Profile profile = user.getProfile();
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        Code code = new Code(uuid, codeRequest.getCode());
        ConnectionPoolService connectionPoolService = ConnectionPoolService.create();

        WebClient webClient = connectionPoolService.getConnection();
        webClient.post()
                .uri("/api/v1/run")
                .bodyValue(BodyInserters.fromValue(code))
                .retrieve()
                .toEntity(CodeResponse.class)   //Change here
                .subscribe(
                        responseEntity -> {
                            // Handle success response here
                            HttpStatusCode status = responseEntity.getStatusCode();
                            URI location = responseEntity.getHeaders().getLocation();
                            CodeResponse createdEmployee = responseEntity.getBody();    // Response body
                            // handle response as necessary
                            code.setRunSuccess(createdEmployee.getStatus());
                            if (!createdEmployee.getStatus().equals("Success")) {
                                code.setErrorMessage(createdEmployee.getCodeResString());
                            }
                            codeService.saveCode(code);
                        },
                        error -> {
                            // Handle the error here
                            if (error instanceof WebClientResponseException) {
                                WebClientResponseException ex = (WebClientResponseException) error;
                                HttpStatusCode status = ex.getStatusCode();
                                System.out.println("Error Status Code: " + status.value());
                                //...
                            } else {
                                // Handle other types of errors
                                System.err.println("An unexpected error occurred: " + error.getMessage());
                            }
                        }
                );
    }
}
