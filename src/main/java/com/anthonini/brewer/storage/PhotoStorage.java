package com.anthonini.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorage {

	public String temporarilySave(MultipartFile[] files);

	public byte[] recoverTemporaryPhoto(String name);
	
	public void save(String photo);

	public byte[] recovery(String nome);

	public byte[] recoverThumbnail(String photo);

	public void remove(String photo);
}
