package com.anthonini.brewer.repository.listener;

import javax.persistence.PostLoad;

import com.anthonini.brewer.BrewerApplication;
import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.storage.PhotoStorage;

public class BeerEntityListener {
	
	@PostLoad
	public void postLoad(final Beer beer) {
		PhotoStorage photoStorage = BrewerApplication.getBean(PhotoStorage.class);
		
		beer.setUrlPhoto(photoStorage.getUrl(beer.getPhotoOrMock()));
		beer.setUrlThumbnailPhoto(photoStorage.getUrl(PhotoStorage.THUMBNAIL_PREFIX + beer.getPhotoOrMock()));
	}
}
