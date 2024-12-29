package com.service.compiler.CodeExecutionService.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebugCodeResponse {
  public String codeResString;
  public String codeDebugResString;
}
