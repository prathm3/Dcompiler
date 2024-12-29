package com.Project.Dompiler.demo.service;

import com.Project.Dompiler.demo.beans.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface LoginService extends UserDetailsService{

	//UserDetails loadUserByEmailId(String emailId);

	User findByEmailID(String username);

	void save(User user);

	User findByUserId(String userId);


}
