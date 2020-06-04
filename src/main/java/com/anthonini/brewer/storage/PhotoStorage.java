package com.anthonini.brewer.storage;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorage {
	
	public final String THUMBNAIL_PREFIX = "thumbnail.";

	public String save(MultipartFile[] files);

	public byte[] recovery(String photo);

	public byte[] recoverThumbnail(String photo);

	public void remove(String photo);

	public String getUrl(String photo);
	
	default String renameFile(String originalFilename) {
		return UUID.randomUUID().toString() + "_" + originalFilename;
	}
}
