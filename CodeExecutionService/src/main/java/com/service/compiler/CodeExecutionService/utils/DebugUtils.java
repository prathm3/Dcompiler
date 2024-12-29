package com.service.compiler.CodeExecutionService.utils;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.StepRequest;

import java.util.Map;

public class DebugUtils {
  public Class debugClass;
  public int[] breakPointLines;
  public VirtualMachine connectAndLaunchVM() throws Exception {

    LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager()
      .defaultConnector();
    Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
    arguments.get("main").setValue(debugClass.getName());
    return launchingConnector.launch(arguments);
  }

  public void enableClassPrepareRequest(VirtualMachine vm) {
    ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
    classPrepareRequest.addClassFilter(debugClass.getName());
    classPrepareRequest.enable();
  }

  public void setBreakPoints(VirtualMachine vm, ClassPrepareEvent event) throws AbsentInformationException {
    ClassType classType = (ClassType) event.referenceType();
    for(int lineNumber: breakPointLines) {
      Location location = classType.locationsOfLine(lineNumber).get(0);
      BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
      bpReq.enable();
    }
  }

  public String displayVariables(LocatableEvent event) throws IncompatibleThreadStateException,
    AbsentInformationException {
    String finalString = "";
    StackFrame stackFrame = event.thread().frame(0);
    if(stackFrame.location().toString().contains(debugClass.getName())) {
      Map<LocalVariable, Value> visibleVariables = stackFrame
        .getValues(stackFrame.visibleVariables());
      finalString += "Variables at " + stackFrame.location().toString() +  " > " + "/n";
      System.out.println("Variables at " + stackFrame.location().toString() +  " > ");
      for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
        finalString += entry.getKey().name() + " = " + entry.getValue() + "/n";
        System.out.println(entry.getKey().name() + " = " + entry.getValue());
      }
    }
    return finalString;
  }

  public void enableStepRequest(VirtualMachine vm, BreakpointEvent event) {
    // enable step request for last break point
    if (event.location().toString().
      contains(debugClass.getName() + ":" + breakPointLines[breakPointLines.length-1])) {
      StepRequest stepRequest = vm.eventRequestManager()
        .createStepRequest(event.thread(), StepRequest.STEP_LINE, StepRequest.STEP_OVER);
      stepRequest.enable();
    }
  }

}
