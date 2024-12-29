package com.service.compiler.CodeExecutionService.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebugCodeRequest {

  public String id;
  public String codeString;
  public int[] breakPoints;

}
