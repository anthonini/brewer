package com.anthonini.brewer.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.repository.ClientRepository;
import com.anthonini.brewer.service.exception.AlreadyRegisteredClientCpfCnpjException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	
	@Transactional()
	public void save(Client client) {
		Optional<Client> existingClient = clientRepository.findByCpfCnpj(client.getCpfCnpjWithoutFormatting());
		
		if(existingClient.isPresent() && !existingClient.get().equals(client)) {
			throw new AlreadyRegisteredClientCpfCnpjException(String.format("%s já cadastrado", client.getPersonType().getDocument()));
		}
		
		clientRepository.save(client);
	}
}
