package com.month.external.upload;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.month.external.upload.dto.component.S3UploaderComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@RequiredArgsConstructor
@Component
public class S3UploaderImpl implements S3Uploader {

	private final AmazonS3Client amazonS3Client;
	private final S3UploaderComponent uploaderComponent;

	public String uploadFile(File file, String fileName) {
		amazonS3Client.putObject(new PutObjectRequest(uploaderComponent.getBucket(), fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3Client.getResourceUrl(uploaderComponent.getBucket(), fileName);
	}

}