package com.anthonini.brewer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.BeerRepository;
import com.anthonini.brewer.service.event.beer.RegisteredBeerEvent;

@Service
public class BeerService {

	@Autowired
	private BeerRepository beerRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Transactional()
	public void save(Beer beer) {
		beerRepository.save(beer);
		
		publisher.publishEvent(new RegisteredBeerEvent(beer));
	}
}
