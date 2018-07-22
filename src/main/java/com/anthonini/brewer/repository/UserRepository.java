package com.anthonini.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthonini.brewer.model.User;
import com.anthonini.brewer.repository.helper.user.UserRepositoryQueries;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQueries {

	public Optional<User> findByEmail(String email);
}
