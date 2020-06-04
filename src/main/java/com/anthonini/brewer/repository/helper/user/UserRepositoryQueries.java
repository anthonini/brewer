package com.anthonini.brewer.repository.helper.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anthonini.brewer.model.User;
import com.anthonini.brewer.repository.filter.UserFilter;

public interface UserRepositoryQueries {

	public Optional<User> byEmailActive(String email);
	
	public List<String> permissions(User user);
	
	public Page<User> filter(UserFilter filter, Pageable pageable);
	
	public User findWithGroups(Long id);
}
