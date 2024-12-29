package com.Project.Dompiler.demo.controllers;

import com.Project.Dompiler.demo.DTO.*;
import com.Project.Dompiler.demo.beans.Profile;
import com.Project.Dompiler.demo.beans.User;
import com.Project.Dompiler.demo.service.LoginService;
import com.Project.Dompiler.demo.service.ProfileService;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

//	@Autowired
//	private RefreshTokenService refreshTokenService;

//	@Autowired
//	private JwtUtit jwtTokenUtil;

	@Autowired
	private LoginService loginService;

	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private JavaMailSender mailSender;
	
//	@Autowired
//	private MailService mailService;
	
//	@Autowired
//    private SendGrid sendGrid;

	@Autowired 
	private PasswordEncoder passwordEncoder;

	//	@Autowired
	//	private BCryptPasswordEncoder bCryptPasswordEncoder;
	//@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/hello")
	public String dummy() {
		return "Hello";
	}
	//@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/signup")
	public ResponseEntity<?> signUpUser(@RequestBody SignUpRequest signUpRequest ){
		//create User
		User u = loginService.findByEmailID(signUpRequest.getEmailId());
		//check if duplicate user avaliable
		if(u !=null) {
			//-----------------------------------------------------------------------
			return ResponseEntity.status(HttpStatus.FOUND).build();
			//throw new InternalServerErrorException("Email Not Found");
		}
		User user = new User();
		//create hashed password

		CharSequence cs = signUpRequest.getPassword();
		System.out.println(cs);
		String encodedPassword = passwordEncoder.encode(cs);
		//update hashed password
		System.out.println(encodedPassword);
		user.setPassword(encodedPassword);
		user.setEmailID(signUpRequest.getEmailId());
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		
		Profile profile = new Profile();
		profile.setFirstName(signUpRequest.getFirstName());
		profile.setLastName(signUpRequest.getLastName());
		profile.setEmailID(signUpRequest.getEmailId());
		profileService.save(profile);
		user.setProfile(profileService.getProfileByEmailId(signUpRequest.getEmailId()));
		
		//save user in db
		loginService.save(user);
		//Login User
		//get user by EmailId
		UserDetails userDetails = loginService.loadUserByUsername(user.getEmailID());
		//generate Token
//		String jwt = jwtTokenUtil.generateToken(userDetails);
		//RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmailID());
		User createdUser = loginService.findByEmailID(user.getEmailID());
//		return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getUsername()));
		return ResponseEntity.ok(new AuthenticationResponse(createdUser.getUserId()));
	}
	//@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
//		String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		return new ResponseEntity("refreshtoken Successfully", HttpStatus.ACCEPTED);
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}
//	@PostMapping("/refreshtoken")
//	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//		String requestRefreshToken = request.getRefreshToken();
//
//		return refreshTokenService.findByToken(requestRefreshToken)
//				.map(refreshTokenService::verifyExpiration)
//				.map(RefreshToken::getUser)
//				.map(user -> {
//					String token = jwtTokenUtil.generateTokenFromUsername(user.getEmailID());
//					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//				})
//				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh Token is not in db"));
//	}
	//@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/signin")
	public ResponseEntity<?> loginUser(@RequestBody SignInRequest signInRequest) throws Exception{
		//check if user exists 
		User u = loginService.findByEmailID(signInRequest.getEmailId());
		if(u == null) {
			System.out.println();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			//throw new InternalServerErrorException("Email Not Found");
		}
		//authenticate
		try {
			//check hashed password
			if(!passwordEncoder.matches(signInRequest.getPassword(), u.getPassword())) {
				throw new BadCredentialsException("Wrong Password");
			}
			//-------------------------------------------------------------------
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmailId(), signInRequest.getPassword()));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			//throw new Exception("Incorrect UserName or Password", e);
		}
		//get UserDetails to login and generate token

		UserDetails userDetails = loginService.loadUserByUsername(signInRequest.getEmailId());
//		String jwt = jwtTokenUtil.generateToken(userDetails);
		User createdUser = loginService.findByEmailID(signInRequest.getEmailId());
		return ResponseEntity.ok(new AuthenticationResponse(createdUser.getUserId()));
	}
	//@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/signout")
	public ResponseEntity<?> signout(HttpServletRequest request, HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return new ResponseEntity("Logout Successfully", HttpStatus.ACCEPTED);
	}
	//@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/forgot")
	public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws UnsupportedEncodingException{
		User u = loginService.findByEmailID(forgotPasswordRequest.getEmailId());
		if(u == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			//throw new InternalServerErrorException("Email Not Found");
		}
		//random otp creating
		String token = "RandomString.make(8)";
		String encodedToken = passwordEncoder.encode(token);
		u.setResetToken(encodedToken);
		//System.out.println(u.getEmailID() + "In forgot controller");
		// create expiresIn for otp
		Date date = new Date();
		u.setExpiresIn(new Date(date.getTime() + 2*(3600)));
		// save new updated user
		loginService.save(u);
		//mail sender
		//mailService.setResetToken(token);
		//mailService.sendTextEmail();
		
//		MimeMessage message = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//		helper.setFrom("prathmeshdeshpande101@gmail.com", "Test");
//		helper.setTo("prathmeshdeshpande101@gmail.com");
//		String subject = "Here is a ";
//		String content = "<p> Hello OTP is "+ token +" Expires in 1hr </p>";
//		helper.setSubject(subject);
//		helper.setText(content, true);
//		mailSender.send(message);
//		Email from = new Email("prathmeshdeshpande2018.mech@mmcoe.edu.in");
//        String subject = "Hello World!";
//        Email to = new Email("prathmeshdeshpande2018.mech@mmcoe.edu.in");
//        Content content = new Content("text/html", "I'm replacing the <strong>body tag</strong>      " + token);
//
//        Mail mail = new Mail(from, subject, to, content);
//
//        mail.setReplyTo(new Email("prathmeshdeshpande2018.mech@mmcoe.edu.in"));
//       // mail.personalization.get(0).addSubstitution("-username-", "Some blog user");
//      //  mail.setTemplateId(EMAIL_TEMPLATE_ID);
//
//        Request request = new Request();
//        Response response = null;
//
//
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//
//            response = sendGrid.api(request);
//
//            System.out.println(response.getStatusCode());
//            System.out.println(response.getBody());
//            System.out.println(response.getHeaders());
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
        
		return new ResponseEntity("token"+token+"user"+ u, HttpStatus.ACCEPTED);
	}
	//@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/reset")
	public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest resetPasswordRequest){
		User u = loginService.findByEmailID(resetPasswordRequest.getEmailId());
		if(u == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			//throw new InternalServerErrorException("Email Not Found"); 
		}
		if(u.getExpiresIn().after(new Date())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			//throw new InternalServerErrorException("Otp is Expired");
		}
		//set password
		String encodedPassword = passwordEncoder.encode(resetPasswordRequest.getPassword());
		System.out.println(encodedPassword);
		u.setPassword(encodedPassword);
		//remove token and expiresin
		u.setResetToken(null);
		u.setExpiresIn(null);
		loginService.save(u);
		return  ResponseEntity.ok(u);
	}
}
