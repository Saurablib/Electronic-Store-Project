package com.lcwd.electronic.store.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	String UploadFiles(MultipartFile file,String path) throws IOException;
	
	InputStream getSource(String path, String name) throws IOException;

}
