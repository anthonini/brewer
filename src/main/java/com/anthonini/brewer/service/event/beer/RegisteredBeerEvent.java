package com.anthonini.brewer.service.event.beer;

import com.anthonini.brewer.model.Beer;

public class RegisteredBeerEvent {

	private Beer beer;
	
	public RegisteredBeerEvent(Beer beer) {
		this.beer = beer;
	}

	public Beer getBeer() {
		return beer;
	}
	
	public boolean hasPhoto() {
		return beer.hasPhoto();
	}
}
