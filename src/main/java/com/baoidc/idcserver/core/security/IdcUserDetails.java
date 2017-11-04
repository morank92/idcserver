package com.baoidc.idcserver.core.security;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.baoidc.idcserver.po.User;

public class IdcUserDetails extends User implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7255177265990196561L;
	private final boolean enabled;
	private final boolean accountNonExpired;
	private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;
    private final Set<GrantedAuthority> authorities;
    
	public IdcUserDetails(User user,boolean enabled,boolean accountNonExpired,
			boolean credentialsNonExpired,boolean accountNonLocked,Set<GrantedAuthority> authorities){
		
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = authorities;
		
		if(user != null && StringUtils.isNotBlank(user.getUserName()) && StringUtils.isNotBlank(user.getPassword())){
			setUserName(user.getUserName());
			setPassword(user.getPassword());
		}else{
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.getPassword();
	}

	public String getUsername() {
		return this.getUsername();
	}

	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
}
