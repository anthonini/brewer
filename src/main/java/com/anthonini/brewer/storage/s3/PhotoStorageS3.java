package com.anthonini.brewer.storage.s3;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.anthonini.brewer.storage.PhotoStorage;

@Profile("prod")
@Component
public class PhotoStorageS3 implements PhotoStorage {

	@Override
	public String save(MultipartFile[] files) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] recovery(String nome) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] recoverThumbnail(String photo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUrl(String photo) {
		// TODO Auto-generated method stub
		return null;
	}

}
