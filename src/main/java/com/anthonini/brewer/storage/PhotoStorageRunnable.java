package com.anthonini.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.anthonini.brewer.dto.PhotoDTO;

public class PhotoStorageRunnable implements Runnable {

	private MultipartFile[] files;
	private DeferredResult<PhotoDTO> result;
	private PhotoStorage photoStorage;
	
	public PhotoStorageRunnable(MultipartFile[] files, DeferredResult<PhotoDTO> result, PhotoStorage photoStorage) {
		this.files = files;
		this.result = result;
		this.photoStorage = photoStorage;
	}

	@Override
	public void run() {
		String name = this.photoStorage.save(files);
		String contentType = files[0].getContentType();
		result.setResult(new PhotoDTO(name, contentType, photoStorage.getUrl(name)));
	}

}
