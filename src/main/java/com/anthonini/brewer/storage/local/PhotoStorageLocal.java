package com.anthonini.brewer.storage.local;

import static java.nio.file.FileSystems.getDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.anthonini.brewer.storage.PhotoStorage;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

@Profile("!prod")
@Component
public class PhotoStorageLocal implements PhotoStorage {

	private static final Logger logger = LoggerFactory.getLogger(PhotoStorageLocal.class);
	private static final String THUMBNAIL_PREFIX = "thumbnail.";
	
	private Path path;
	
	public PhotoStorageLocal() {
		this(getDefault().getPath(System.getenv("HOME"), ".brewerphotos"));
	}
	
	public PhotoStorageLocal(Path path) {
		this.path = path;
		criarPastas();
	}

	@Override
	public String save(MultipartFile[] files) {
		String newName = null;
		if (files != null && files.length > 0) {
			MultipartFile file = files[0];
			newName = renameFile(file.getOriginalFilename());
			try {
				file.transferTo(new File(this.path.toAbsolutePath().toString() + getDefault().getSeparator() + newName));
			} catch (IOException e) {
				throw new RuntimeException("Error saving photo.", e);
			}
		}
		
		try {
			Thumbnails.of(this.path.resolve(newName).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Erro generating thumbnail", e);
		}
		
		return newName;
	}
	
	@Override
	public byte[] recovery(String name) {
		try {
			return Files.readAllBytes(this.path.resolve(name));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading photo.", e);
		}
	}
	
	@Override
	public byte[] recoverThumbnail(String photo) {
		return recovery(THUMBNAIL_PREFIX + photo);
	}
	
	@Override
	public void remove(String photo) {
		try {
			Files.deleteIfExists(this.path.resolve(photo));
		} catch (IOException e) {
			logger.warn(String.format("Erro ao apagar foto %s. Mensagem: %s", photo, e.getMessage()));
		}
		
		try {
			Files.deleteIfExists(this.path.resolve(THUMBNAIL_PREFIX + photo));
		} catch (IOException e) {
			logger.warn(String.format("Erro ao apagar a thumbnail da foto %s. Mensagem: %s", photo, e.getMessage()));
		}
	}
	
	@Override
	public String getUrl(String photo) {
		return "http://localhost:8080/brewer/photos/" + photo;
	}
	
	private void criarPastas() {
		try {
			Files.createDirectories(this.path);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Created photo folders.");
				logger.debug("Default folder: " + this.path.toAbsolutePath());
			}
		} catch (IOException e) {
			throw new RuntimeException("Error creating folder for saving photo", e);
		}
	}
}