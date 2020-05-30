package com.anthonini.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthonini.brewer.model.User;
import com.anthonini.brewer.repository.helper.user.UserRepositoryQueries;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQueries {

	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByEmailOrId(String email, Long id);

	public List<User> findByIdIn(Long[] ids);
}
