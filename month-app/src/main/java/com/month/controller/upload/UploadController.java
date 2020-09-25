package com.month.controller.upload;

import com.month.controller.ApiResponse;
import com.month.service.upload.UploadService;
import com.month.type.UploadType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class UploadController {

	private final UploadService uploadService;

	@PostMapping("/api/v1/upload")
	public ApiResponse<String> upload(@RequestParam MultipartFile file, @RequestParam UploadType type) {
		return ApiResponse.of(uploadService.upload(file, type.getDirectory()));
	}

}
