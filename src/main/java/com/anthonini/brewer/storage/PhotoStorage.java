package com.anthonini.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorage {

	public String temporarilySave(MultipartFile[] files);

	public byte[] recoverTemporaryPhoto(String name);
}
