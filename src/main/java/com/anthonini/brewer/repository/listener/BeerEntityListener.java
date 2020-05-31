package com.anthonini.brewer.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.storage.PhotoStorage;

public class BeerEntityListener {

	@Autowired
	private PhotoStorage photoStorage;
	
	@PostLoad
	public void postLoad(final Beer beer) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		beer.setUrlPhoto(photoStorage.getUrl(beer.getPhotoOrMock()));
		beer.setUrlThumbnailPhoto(photoStorage.getUrl(PhotoStorage.THUMBNAIL_PREFIX + beer.getPhotoOrMock()));
	}
}
