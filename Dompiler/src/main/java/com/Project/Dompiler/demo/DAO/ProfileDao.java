package com.Project.Dompiler.demo.DAO;

import com.Project.Dompiler.demo.beans.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileDao extends JpaRepository<Profile, String> {

	Profile findByEmailID(String emailId);

}
