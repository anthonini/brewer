package com.anthonini.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.model.User;
import com.anthonini.brewer.repository.UserRepository;
import com.anthonini.brewer.security.SystemUser;
import com.anthonini.brewer.service.exception.NotPossibleDeleteUserException;
import com.anthonini.brewer.service.exception.UserCannotRemoveYourselfException;
import com.anthonini.brewer.service.exception.UserEmailAlreadyRegisteredException;
import com.anthonini.brewer.service.exception.UserPasswordRequiredException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public void save(User user) {
		Optional<User> existingUserByEmail = userRepository.findByEmailOrId(user.getEmail(), user.getId());
		
		if(existingUserByEmail.isPresent() && !existingUserByEmail.get().equals(user)) {
			throw new UserEmailAlreadyRegisteredException("E-mail já cadastrado");
		}
		
		if(user.isNew() && StringUtils.isEmpty(user.getPassword())) {
			throw new UserPasswordRequiredException("Senha é obrigatória");
		}
		
		if( user.isNew() || !StringUtils.isEmpty(user.getPassword())) {
			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			user.setPasswordConfirmation(user.getPassword());
		}
		
		if(!user.isNew()) {
			if(StringUtils.isEmpty(user.getPassword())) {
				user.setPassword(existingUserByEmail.get().getPassword());
			}
			if(user.getActive() == null) {
				user.setActive(existingUserByEmail.get().getActive());
			}
		}
		
		userRepository.save(user);
	}

	@Transactional
	public void updateStatus(Long[] ids, UserStatus userStatus) {
		for(Long id : ids) {
			User user = new User(id);
			validateSameUser(user);
		}
		userStatus.executar(ids, userRepository);
	}

	@Transactional
	public void delete(User user) {
		validateSameUser(user);
		try {
			userRepository.delete(user);
			userRepository.flush();
		} catch (PersistenceException e) {
			throw new NotPossibleDeleteUserException("Não é possivel apagar o usuário. Usuário já efetuou alguma venda.");
		}
	}

	private void validateSameUser(User user) {
		SystemUser systemUser = (SystemUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(systemUser.getUser().equals(user)) {
			throw new UserCannotRemoveYourselfException();
		}
	}
}
