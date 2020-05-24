package com.anthonini.brewer.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SystemUser extends User {

	private static final long serialVersionUID = 1L;
	
	private com.anthonini.brewer.model.User user;

	public SystemUser(com.anthonini.brewer.model.User user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getEmail(), user.getPassword(), authorities);
		this.user = user;
	}

	public com.anthonini.brewer.model.User getUser() {
		return user;
	}

}
