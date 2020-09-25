package com.month.service.upload;

import com.month.external.upload.S3Uploader;
import com.month.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RequiredArgsConstructor
@Service
public class UploadService {

	private final S3Uploader s3Uploader;

	public String upload(MultipartFile multipartFile, String dirName) {
		File file = FileUtils.convertToFile(multipartFile, FileUtils.getUniqueImageFileName(multipartFile));
		String url = uploadToStorage(file, dirName);
		FileUtils.removeTempFile(file);
		return url;
	}

	private String uploadToStorage(File file, String dirName) {
		String fileName = dirName + "/" + file.getName();
		return s3Uploader.uploadFile(file, fileName);
	}

}
