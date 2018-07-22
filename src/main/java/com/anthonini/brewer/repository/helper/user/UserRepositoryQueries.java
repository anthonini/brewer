package com.anthonini.brewer.repository.helper.user;

import java.util.List;
import java.util.Optional;

import com.anthonini.brewer.model.User;

public interface UserRepositoryQueries {

	public Optional<User> byEmailActive(String email);
	
	public List<String> permissions(User user);
}
