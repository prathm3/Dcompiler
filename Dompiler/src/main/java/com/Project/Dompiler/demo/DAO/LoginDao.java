package com.Project.Dompiler.demo.DAO;

import com.Project.Dompiler.demo.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDao extends JpaRepository<User, String> {

	User findByEmailID(String username);

}
