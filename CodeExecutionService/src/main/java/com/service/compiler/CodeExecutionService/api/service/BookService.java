package com.service.compiler.CodeExecutionService.api.service;

import com.service.compiler.CodeExecutionService.api.model.CodeRequest;
import com.service.compiler.CodeExecutionService.api.model.CodeResponse;
import com.service.compiler.CodeExecutionService.api.model.DebugCodeRequest;
import com.service.compiler.CodeExecutionService.api.model.DebugCodeResponse;
import com.service.compiler.CodeExecutionService.utils.DebugUtils;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);
    public BookService() {
    }
  public Future<CodeResponse> compile(CodeRequest codeRequest) {
    System.out.println("main" + codeRequest);
    String code = codeRequest.getCodeString();
    String id = codeRequest.getId();

    CodeResponse codeResponse = new CodeResponse();

		try {
			System.out.println("main" + "try");
			Files.write(Paths.get("Main.java"), code.getBytes());
			Process process = Runtime.getRuntime().exec("javac Main.java");
			process.waitFor();
			System.out.println("main" + "process]");

			if(process.exitValue() == 0) {
				System.out.println("main" + "exitValue");
        codeResponse.setStatus("Success");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String input = reader.lines().collect(Collectors.joining("\n"));
				codeResponse.setCodeResString(input);
			} else {
        codeResponse.setStatus("Failure");
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String output = reader1.lines().collect(Collectors.joining("\n"));
        codeResponse.setCodeResString(output);
      }
		} catch (Exception e) {
      codeResponse.setStatus("Failure");
      codeResponse.setCodeResString(e.getMessage());
      System.out.println(e.getMessage());
		}
    return Future.succeededFuture(codeResponse);
  }
  public Future<CodeResponse> run(CodeRequest codeRequest) {
    System.out.println("main" + codeRequest);
    String code = codeRequest.getCodeString();
    String id = codeRequest.getId();

    CodeResponse codeResponse = new CodeResponse();

    try {
      System.out.println("main" + "try");
      Files.write(Paths.get("Main.java"), code.getBytes());
      Process process = Runtime.getRuntime().exec("javac Main.java");
      process.waitFor();
      System.out.println("main" + "process]");

      if(process.exitValue() == 0) {
        System.out.println("main" + "exitValue");
				Process runProcess = Runtime.getRuntime().exec("java Main");
				runProcess.waitFor();
				if(runProcess.exitValue() == 0) {
          codeResponse.setStatus("Success");
					BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
					String output = reader.lines().collect(Collectors.joining("\n"));
					System.out.println("Main" + output);
          codeResponse.setCodeResString(output);
				} else {
					BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
					String output = reader.lines().collect(Collectors.joining("\n"));
					System.out.println("ERROR");
          codeResponse.setStatus("Failure");
          codeResponse.setCodeResString(output);
        }
      } else {
        codeResponse.setStatus("Failure");
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String output = reader1.lines().collect(Collectors.joining("\n"));
        codeResponse.setCodeResString(output);
      }
    } catch (Exception e) {
      codeResponse.setStatus("Failure");
      codeResponse.setCodeResString(e.getMessage());
      System.out.println(e.getMessage());
    }
    return Future.succeededFuture(codeResponse);
  }

  public Future<DebugCodeResponse> debug(DebugCodeRequest codeRequest) {
    System.out.println("main" + codeRequest);
    String code = codeRequest.getCodeString();
    String id = codeRequest.getId();
    int[] breakPoints = codeRequest.getBreakPoints();

    DebugCodeResponse codeResponse = new DebugCodeResponse();

    try {
      System.out.println("main" + "try");
      Files.write(Paths.get("Main.java"), code.getBytes());

      DebugUtils debuggerInstance = new DebugUtils();
      Process compileProcess = Runtime.getRuntime().exec("javac Main.java");
      compileProcess.waitFor();
      if (compileProcess.exitValue() == 0) {
        System.out.println("Compilation successful, Main.class generated.");
      } else {
        System.out.println("Compilation failed.");
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
        String errorLine;
        String finalString = "";
        while ((errorLine = errorReader.readLine()) != null) {
          finalString += errorLine;
          System.out.println(errorLine);
        }
        codeResponse.setCodeResString(finalString);
        return Future.succeededFuture(codeResponse);
      }
      System.out.println("Here is isuse");
      try {

        // Check if Main.class exists in the expected location
        File f = new File("Main.class");  // This checks if the file exists in the root directory
        if(f.exists()) {
          System.out.println("Main.class exists in the expected location.");
        } else {
          System.out.println("Main.class not found.");
        }

        // Load the class dynamically using Class.forName()
        Class<?> mainClass = Class.forName("Main");  // Ensure Main.class is in the classpath

        // Print out class name and try to find the resource (class file location)
        System.out.println(mainClass.getName() + " has been loaded.");
        debuggerInstance.debugClass = mainClass;

        // Correct usage of getResource() to find the class file
        System.out.println("Class " + mainClass.getName() + " is loaded from: " + mainClass.getResource("/" + mainClass.getName().replace(".", "/") + ".class"));

      } catch (ClassNotFoundException e) {
        System.out.println("Main class not found.");
        e.printStackTrace();
      }

      debuggerInstance.breakPointLines = (breakPoints);
      System.out.println(debuggerInstance);

      VirtualMachine vm = null;
      try {
        vm = debuggerInstance.connectAndLaunchVM();
        debuggerInstance.enableClassPrepareRequest(vm);
        EventSet eventSet = null;
        String finalString = "";
        System.out.println("her vm");

        while ((eventSet = vm.eventQueue().remove()) != null) {
          System.out.println("her event");

          for (Event event : eventSet) {
            System.out.println("here event loop");

            if (event instanceof ClassPrepareEvent) {
              System.out.println("Want to go next ");

              debuggerInstance.setBreakPoints(vm, (ClassPrepareEvent)event);
            }
            if (event instanceof BreakpointEvent) {
              System.out.println("Want to go next ");

              finalString +=  debuggerInstance.displayVariables((BreakpointEvent) event);

            }
            if (event instanceof StepEvent) {
              System.out.println("Want to go next ");

              finalString +=  debuggerInstance.displayVariables((StepEvent) event);

            }
            vm.resume();
          }
        }
        System.out.println(finalString);
        codeResponse.setCodeDebugResString(finalString);
      } catch (VMDisconnectedException e) {
        System.out.println("Virtual Machine is disconnected.");
        return Future.succeededFuture(codeResponse);
      } catch (Exception e) {
        e.printStackTrace();
        return Future.succeededFuture(codeResponse);
      }
    } catch (Exception e) {
      codeResponse.setCodeResString(e.getMessage());
      System.out.println(e.getMessage());
      return Future.succeededFuture(codeResponse);

    }
    return Future.succeededFuture(codeResponse);
  }


//  public Future<BookGetAllRespo> readAll(String p,
//                                              String l) {
//        return dbClient.withTransaction(
//                connection -> {
//                    final int page = QueryUtils.getPage(p);
//                    final int limit = QueryUtils.getLimit(l);
//                    final int offset = QueryUtils.getOffset(page, limit);
//
//                    return bookRepository.count(connection)
//                            .flatMap(total ->
//                                    bookRepository.selectAll(connection, limit, offset)
//                                            .map(result -> {
//                                                final List<BookGetByIdResponse> books = result.stream()
//                                                        .map(BookGetByIdResponse::new)
//                                                        .collect(Collectors.toList());
//
//                                                return new BookGetAllResponse(total, limit, page, books);
//                                            })
//                            );
//                })
//                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Read all books", success.getBooks())))
//                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Read all books", throwable.getMessage())));
//    }
//
//    public Future<BookGetByIdResponse> readOne(int id) {
//        return dbClient.withTransaction(
//                connection -> {
//                    return bookRepository.selectById(connection, id)
//                            .map(BookGetByIdResponse::new);
//                })
//                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Read one book", success)))
//                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Read one book", throwable.getMessage())));
//    }
//
//    public Future<BookGetByIdResponse> create(Book book) {
//        return dbClient.withTransaction(
//                connection -> {
//                    return bookRepository.insert(connection, book)
//                            .map(BookGetByIdResponse::new);
//                })
//                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Create one book", success)))
//                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Create one book", throwable.getMessage())));
//    }
//
//    public Future<BookGetByIdResponse> update(int id,
//                                              Book book) {
//        book.setId(id);
//
//        return dbClient.withTransaction(
//                connection -> {
//                    return bookRepository.update(connection, book)
//                            .map(BookGetByIdResponse::new);
//                })
//                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Update one book", success)))
//                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Update one book", throwable.getMessage())));
//    }
//
//    public Future<Void> delete(Integer id) {
//        return dbClient.withTransaction(
//                connection -> {
//                    return bookRepository.delete(connection, id);
//                })
//                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Delete one book", id)))
//                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Delete one book", throwable.getMessage())));
//    }

}
