package com.anthonini.brewer.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anthonini.brewer.model.User;
import com.anthonini.brewer.repository.UserRepository;
import com.anthonini.brewer.service.exception.UserEmailAlreadyRegisteredException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public void save(User user) {
		Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
		
		if(existingUserByEmail.isPresent()) {
			throw new UserEmailAlreadyRegisteredException("E-mail já cadastrado");
		}
		
		if( user.isNew() ) {
			String encryptedPassword = this.passwordEncoder.encode(user.getPassword());
			user.setPassword(encryptedPassword);
			user.setPasswordConfirmation(encryptedPassword);
		}
		
		userRepository.save(user);
	}

	@Transactional
	public void updateStatus(Long[] ids, UserStatus userStatus) {
		userStatus.executar(ids, userRepository);
	}
}
