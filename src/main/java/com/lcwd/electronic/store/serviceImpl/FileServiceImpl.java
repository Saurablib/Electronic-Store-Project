package com.lcwd.electronic.store.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.exception.BadRequestException;
import com.lcwd.electronic.store.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String UploadFiles(MultipartFile file, String path) throws IOException {
		
		String originalFilename = file.getOriginalFilename();
		String fileName = UUID.randomUUID().toString();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileNameWithExtension = fileName + extension;		
		String fullPathWithFileName = path + File.separator + fileNameWithExtension;
		
		
		if(extension.equalsIgnoreCase(".png")|| extension.equalsIgnoreCase(".jpg")|| extension.equalsIgnoreCase(".jpeg")||
				extension.equalsIgnoreCase(".svg")) {
			
			File folder = new File(path);
			
			if(!folder.exists()) {
				folder.mkdirs();
			}
			
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, Paths.get(fullPathWithFileName), StandardCopyOption.REPLACE_EXISTING);
			}
			return fileNameWithExtension;
			
		}else {
			throw new BadRequestException("File ");
		}
	}

	@Override
	public InputStream getSource(String path, String name) throws IOException {
		
		String fullPath = path+name;
		InputStream inputStream = new FileInputStream(fullPath);
	
		return inputStream;
	}

}
