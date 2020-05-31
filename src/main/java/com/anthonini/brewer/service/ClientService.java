package com.anthonini.brewer.service;


import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.repository.ClientRepository;
import com.anthonini.brewer.service.exception.AlreadyRegisteredClientCpfCnpjException;
import com.anthonini.brewer.service.exception.NotPossibleDeleteEntityException;

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

	@Transactional
	public void delete(Client client) {
		try {
			clientRepository.delete(client);
			clientRepository.flush();
		} catch (PersistenceException e) {
			throw new NotPossibleDeleteEntityException("Não é possivel apagar o cliente. Já foi realizada alguma venda para o cliente.");
		}
	}
	
	
}
