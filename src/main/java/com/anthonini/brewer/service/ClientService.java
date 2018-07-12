package com.anthonini.brewer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.repository.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	
	@Transactional()
	public void save(Client client) {
		clientRepository.save(client);
	}
}
