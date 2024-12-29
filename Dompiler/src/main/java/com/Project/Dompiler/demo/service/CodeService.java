package com.Project.Dompiler.demo.service;

import com.Project.Dompiler.demo.beans.Code;
import com.Project.Dompiler.demo.beans.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CodeService {

    List<Code> getCodesByProfileId(Profile profile);

    boolean saveCode(Code code);

    void updateCode(Code code, String id);
}
