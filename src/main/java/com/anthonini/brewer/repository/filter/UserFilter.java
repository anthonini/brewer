package com.anthonini.brewer.repository.filter;

import java.util.List;

import com.anthonini.brewer.model.UserGroup;

public class UserFilter {

	private String name;
	private String email;
	private List<UserGroup> groups;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<UserGroup> getGroups() {
		return groups;
	}
	
	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}
}
