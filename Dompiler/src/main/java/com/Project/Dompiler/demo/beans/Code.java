package com.Project.Dompiler.demo.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Code")
public class Code {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name="code_id" , columnDefinition="default 'defaultAdmin'")
    private String id;

    private String codeString;

    private String compiledSuccess;

    private String runSuccess;

    private String errorMessage;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "profileId")
    @JsonIgnore
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    public String getCodeString() {
        return codeString;
    }
    public void setCodeString(String codeString) {
        this.codeString = codeString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Code(String id, String codeString) {
        this.id = id;
        this.codeString = codeString;
    }

    public Code(String codeString) {
        this.codeString = codeString;
    }

    public Code(String id, String codeString, Profile profile) {
        this.id = id;
        this.codeString = codeString;
        this.profile = profile;
        this.compiledSuccess = "";
        this.runSuccess = "";
    }

    public String isCompiledSuccess() {
        return compiledSuccess;
    }

    public String isRunSuccess() {
        return runSuccess;
    }

    public void setCompiledSuccess(String compiledSuccess) {
        this.compiledSuccess = compiledSuccess;
    }

    public void setRunSuccess(String runSuccess) {
        this.runSuccess = runSuccess;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id='" + id + '\'' +
                ", code='" + codeString + '\'' +
                ", compiledSuccess=" + compiledSuccess +
                ", runSuccess=" + runSuccess +
                ", profile=" + profile +
                '}';
    }
}
