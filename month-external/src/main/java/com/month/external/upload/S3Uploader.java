package com.month.external.upload;

import java.io.File;

public interface S3Uploader {

	String uploadFile(File file, String fileName);

}
