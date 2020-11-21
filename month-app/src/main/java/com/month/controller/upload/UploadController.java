package com.month.controller.upload;

import com.month.controller.ApiResponse;
import com.month.service.upload.UploadService;
import com.month.type.UploadType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class UploadController {

	private final UploadService uploadService;

	@ApiOperation("파일 업로드를 위한 API")
	@PostMapping("/api/v1/upload")
	public ApiResponse<String> uploadImageFile(@RequestParam MultipartFile file, @RequestParam UploadType type) {
		return ApiResponse.of(uploadService.upload(file, type.getDirectory()));
	}

}
