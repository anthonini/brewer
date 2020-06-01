package com.anthonini.brewer.storage.s3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.anthonini.brewer.storage.PhotoStorage;

import net.coobird.thumbnailator.Thumbnails;

@Profile("prod")
@Component
public class PhotoStorageS3 implements PhotoStorage {
	
	private static final Logger logger = LoggerFactory.getLogger(PhotoStorageS3.class);
	
	private static final String BUCKET = "anbrewer";
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Override
	public String save(MultipartFile[] files) {
		String newName = null;
		if(files != null && files.length > 0) {
			MultipartFile file = files[0];
			newName = renameFile(file.getOriginalFilename());
			
			try {
				AccessControlList accessControlList = new AccessControlList();
				accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
				
				sendPhoto(newName, file, accessControlList);
				sendThumbnail(newName, file, accessControlList);
			} catch (IOException e) {
				throw new RuntimeException("Erro ao salvar arquivo no S3", e);
			}
		}
		
		return newName;
	}

	@Override
	public byte[] recovery(String photo) {
		InputStream is = amazonS3.getObject(BUCKET, photo).getObjectContent();
		try {
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			logger.error("Não foi possível recuperar a photo do S3", e);
		}
		
		return null;
	}

	@Override
	public byte[] recoverThumbnail(String photo) {
		return recovery(PhotoStorage.THUMBNAIL_PREFIX + photo);
	}

	@Override
	public void remove(String photo) {
		amazonS3.deleteObjects(new DeleteObjectsRequest(BUCKET).withKeys(photo, THUMBNAIL_PREFIX + photo));
	}

	@Override
	public String getUrl(String photo) {
		if(!StringUtils.isEmpty(photo)) {
			return "https://anbrewer.s3-us-west-2.amazonaws.com/" + photo;
		}
		
		return null;
	}

	private void sendPhoto(String newName, MultipartFile file, AccessControlList accessControlList) throws IOException {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());
		
		amazonS3.putObject(new PutObjectRequest(BUCKET, newName, file.getInputStream(), metadata)
				.withAccessControlList(accessControlList));
	}

	private void sendThumbnail(String newName, MultipartFile file, AccessControlList accessControlList) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Thumbnails.of(file.getInputStream()).size(40, 68).toOutputStream(os);
		byte[] array = os.toByteArray();
		InputStream is = new ByteArrayInputStream(array);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(array.length);
		
		amazonS3.putObject(new PutObjectRequest(BUCKET, THUMBNAIL_PREFIX + newName, is, metadata)
				.withAccessControlList(accessControlList));
	}
}
