package com.Project.Dompiler.demo.DTO;

import java.util.Date;

public class CodeRequest {

    private String code;
    private Date codeCreatedAt;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCodeCreatedAt() {
        return codeCreatedAt;
    }

    public void setCodeCreatedAt(Date codeCreatedAt) {
        this.codeCreatedAt = codeCreatedAt;
    }

    public CodeRequest(String code, Date recordCreatedAt) {
        this.code = code;
        this.codeCreatedAt = recordCreatedAt;
    }
    @Override
    public String toString() {
        return "CodeRequest{" +
                "code='" + code + '\'' +
                ", codeCreatedAt=" + codeCreatedAt +
                '}';
    }
}
