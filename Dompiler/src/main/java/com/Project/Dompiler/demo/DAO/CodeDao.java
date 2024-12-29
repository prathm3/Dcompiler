package com.Project.Dompiler.demo.DAO;

import com.Project.Dompiler.demo.beans.Code;
import com.Project.Dompiler.demo.beans.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeDao extends JpaRepository<Code, String> {

    List<Code> findByProfile(Profile profile);

}
