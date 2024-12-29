package com.service.compiler.CodeExecutionService.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationUtils {
	private Integer serverPort;


	public static int numberOfAvailableCores() {
		// I divide this in half to save some resources while developing
		return Runtime.getRuntime().availableProcessors() / 2;
	}
}
