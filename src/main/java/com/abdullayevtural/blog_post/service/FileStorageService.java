package com.abdullayevtural.blog_post.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

	public FileStorageService() {
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new RuntimeException("Faylların saxlanılması üçün qovluq yaradılmadı!", ex);
		}
	}

	public String storeFile(MultipartFile file) {
		String originalFileName = file.getOriginalFilename();

		String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

		try {
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new RuntimeException("Fayl saxlanıla bilmədi: " + fileName, ex);
		}
	}
}