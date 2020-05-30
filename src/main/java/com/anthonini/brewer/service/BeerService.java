package com.anthonini.brewer.service;


import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.BeerRepository;
import com.anthonini.brewer.service.event.beer.RegisteredBeerEvent;
import com.anthonini.brewer.service.exception.NotPossibleDeleteEntityException;
import com.anthonini.brewer.storage.PhotoStorage;

@Service
public class BeerService {

	@Autowired
	private BeerRepository beerRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private PhotoStorage photoStorage;
	
	@Transactional()
	public void save(Beer beer) {
		beerRepository.save(beer);
		
		publisher.publishEvent(new RegisteredBeerEvent(beer));
	}
	
	@Transactional
	public void delete(Beer beer) {
		try {
			String photo = beer.getPhoto();
			beerRepository.delete(beer);
			beerRepository.flush();
			photoStorage.remove(photo);
		} catch (PersistenceException e) {
			throw new NotPossibleDeleteEntityException("Não é possivel apagar a cerveja. Já usada em alguma venda.");
		}
	}
}
