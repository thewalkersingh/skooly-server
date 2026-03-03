package com.skooly.utils;
import com.skooly.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Component
public class FileUploadUtils {
	@Value("${app.upload.dir:uploads}")
	private String uploadDir;
	private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB
	private static final String[] ALLOWED_TYPES = {"image/jpeg", "image/png", "image/webp"};
	
	public String uploadFile(MultipartFile file, String folder) {
		validateFile(file);
		try{
			String filename = UUID.randomUUID()+"_"+file.getOriginalFilename();
			Path dir = Paths.get(uploadDir, folder);
			Files.createDirectories(dir);
			Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			return folder+"/"+filename;
		} catch(IOException e){
			throw new BadRequestException("Failed to upload file: "+e.getMessage());
		}
	}
	
	public void deleteFile(String filePath) {
		try{
			Path path = Paths.get(uploadDir, filePath);
			Files.deleteIfExists(path);
		} catch(IOException ignored){ }
	}
	
	private void validateFile(MultipartFile file) {
		if(file.isEmpty())
			throw new BadRequestException("File is empty");
		if(file.getSize() > MAX_SIZE)
			throw new BadRequestException("File size exceeds 5MB");
		String contentType = file.getContentType();
		for(String type : ALLOWED_TYPES){
			if(type.equals(contentType))
				return;
		}
		throw new BadRequestException("Invalid file type. Only JPEG, PNG, WEBP allowed");
	}
}