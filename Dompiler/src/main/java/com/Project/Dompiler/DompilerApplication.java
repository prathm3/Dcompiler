package com.Project.Dompiler;

import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@SpringBootApplication
public class DompilerApplication {

	public static void main(String[] args) {
		try {
			String path = "javac -g";
			Process process = Runtime.getRuntime().exec(path + " src/main/java/com/Project/Dompiler/demo/*.java");
			process.waitFor();
			if(process.exitValue() == 0) {
				System.out.println("Compiler Success");
				File classFile = new File("F:\\Dcompiler\\Dompiler\\src\\main\\java\\com\\Project\\Dompiler\\demo\\JDIExampleDebugger.class");
				if (classFile.exists()) {
					System.out.println("Class file found: " + classFile.getPath());
				} else {
					System.out.println("Class file not found! Check directory structure and package.");
					return;
				}

				Process runProcess = Runtime.getRuntime().exec("java " + "F:\\Dcompiler\\Dompiler\\src\\main\\java\\com\\Project\\Dompiler\\demo\\JDIExampleDebugger.java");
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));

				runProcess.waitFor();

				// Capture output
				String line;
				while ((line = inputReader.readLine()) != null) {
					System.out.println("Output: " + line);
				}

				// Capture errors
				while ((line = errorReader.readLine()) != null) {
					System.out.println("Error: " + line);
				}

				if (runProcess.exitValue() == 0) {
					System.out.println("Run Success");
				} else {
					System.out.println("Run Failed with exit code " + runProcess.exitValue());
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		SpringApplication.run(DompilerApplication.class, args);
	}

}
