package com.Project.Dompiler.demo.utils;

import com.Project.Dompiler.demo.beans.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service("assembler")
public class Assembler {

	@Transactional()
	public User buildUserFromUserEntity(User userEntity) {

		String username = userEntity.getEmailID();

		String password = userEntity.getPassword();

		boolean enabled = true;

		boolean accountNonExpired = true;

		boolean credentialsNonExpired = true;

		boolean accountNonLocked = true;

		Collection authorities = new ArrayList();


//		User user = new User(username, password, enabled,
//
//				accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

		return null;

	}

}