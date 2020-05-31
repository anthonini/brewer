package com.anthonini.brewer.service.event.beer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.anthonini.brewer.storage.PhotoStorage;

@Component
public class BeerListener {

	@Autowired
	private PhotoStorage photoStorage;
	
	@EventListener(condition = "#event.hasPhoto() and #event.newPhoto")
	public void RegisteredBeer(RegisteredBeerEvent event) {
		photoStorage.save(event.getBeer().getPhoto());
	}
}
