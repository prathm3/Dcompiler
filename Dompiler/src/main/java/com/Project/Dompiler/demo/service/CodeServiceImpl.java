package com.Project.Dompiler.demo.service;

import com.Project.Dompiler.demo.DAO.CodeDao;
import com.Project.Dompiler.demo.DAO.ProfileDao;
import com.Project.Dompiler.demo.beans.Code;
import com.Project.Dompiler.demo.beans.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodeServiceImpl implements CodeService{

    @Autowired
    private CodeDao codesDao;

    @Override
    public List<Code> getCodesByProfileId(Profile profile) {
        return codesDao.findByProfile(profile);
    }

    @Override
    public boolean saveCode(Code code) {
        codesDao.save(code);
        return true;
    }

    @Override
    public void updateCode(Code code, String id) {
        Optional<Code> op = codesDao.findById(id);
        if(op.isPresent()) {
            Code record = op.get();
            record.setCompiledSuccess(code.isCompiledSuccess());
            record.setRunSuccess(code.isRunSuccess());
            codesDao.save(record);
        }
    }

}
