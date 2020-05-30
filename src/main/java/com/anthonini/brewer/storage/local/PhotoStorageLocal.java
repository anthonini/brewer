package com.anthonini.brewer.storage.local;

import static java.nio.file.FileSystems.getDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.anthonini.brewer.storage.PhotoStorage;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

public class PhotoStorageLocal implements PhotoStorage {

	private static final Logger logger = LoggerFactory.getLogger(PhotoStorageLocal.class);
	private static final String THUMBNAIL_PREFIX = "thumbnail.";
	
	private Path path;
	private Path temporaryPath;
	
	public PhotoStorageLocal() {
		this(getDefault().getPath(System.getenv("HOME"), ".brewerphotos"));
	}
	
	public PhotoStorageLocal(Path path) {
		this.path = path;
		criarPastas();
	}

	@Override
	public String temporarilySave(MultipartFile[] files) {
		String newName = null;
		if (files != null && files.length > 0) {
			MultipartFile file = files[0];
			newName = renameFile(file.getOriginalFilename());
			try {
				file.transferTo(new File(this.temporaryPath.toAbsolutePath().toString() + getDefault().getSeparator() + newName));
			} catch (IOException e) {
				throw new RuntimeException("Error saving photo in temporarily folder.", e);
			}
		}
		
		return newName;
	}
	
	@Override
	public byte[] recoverTemporaryPhoto(String name) {
		try {
			return Files.readAllBytes(this.temporaryPath.resolve(name));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading temporary photo.", e);
		}
	}
	
	@Override
	public void save(String photo) {
		try {
			Files.move(this.temporaryPath.resolve(photo),this.path.resolve(photo));
		} catch (IOException e) {
			throw new RuntimeException("Error moving photo to final destination.", e);
		}
		
		try {
			Thumbnails.of(this.path.resolve(photo).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Erro generating thumbnail", e);
		}
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
	
	private void criarPastas() {
		try {
			Files.createDirectories(this.path);
			this.temporaryPath = getDefault().getPath(this.path.toString(), "temp");
			Files.createDirectories(this.temporaryPath);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Created photo folders.");
				logger.debug("Default folder: " + this.path.toAbsolutePath());
				logger.debug("Temporary Folder: " + this.temporaryPath.toAbsolutePath());
			}
		} catch (IOException e) {
			throw new RuntimeException("Error creating folder for saving photo", e);
		}
	}

	private String renameFile(String originalFilename) {
		String newName = UUID.randomUUID().toString() + "_" + originalFilename;
		
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Original name: %s, new name: %s", originalFilename, newName));
		}
		
		return newName;
	}
}