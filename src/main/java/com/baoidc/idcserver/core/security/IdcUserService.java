package com.baoidc.idcserver.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.baoidc.idcserver.dao.IUserDAO;
import com.baoidc.idcserver.po.User;

public class IdcUserService implements UserDetailsService {
	
	@Autowired
	private IUserDAO userDAO;

	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {
		User user = userDAO.getUserInfoByEmail(email);
		if(user == null){
			throw new UsernameNotFoundException("Could not find the user '" + email + "'");
		}
		return new IdcUserDetails(user, true, true, true, true, null);
	}

}
