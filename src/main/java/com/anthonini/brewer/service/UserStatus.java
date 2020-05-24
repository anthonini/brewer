package com.anthonini.brewer.service;

import com.anthonini.brewer.repository.UserRepository;

public enum UserStatus {

	ACTIVATE {
		@Override
		public void executar(Long[] ids, UserRepository userRepository) {
			userRepository.findByIdIn(ids).forEach(u -> u.setActive(true));
		}
		
	},
	DEACTIVATE {
		@Override
		public void executar(Long[] ids, UserRepository userRepository) {
			userRepository.findByIdIn(ids).forEach(u -> u.setActive(false));
		}
	};
	
	
	public abstract void executar(Long[] ids, UserRepository userRepository);
}
