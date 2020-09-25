package com.month.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public final class FileUtils {

	private final static String DOT = ".";
	private final static String TEMP = "month-app/src/main/resources/tmp/";

	private final static List<String> allowImgExtensions = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png"));

	/**
	 * MultipartFile 의 확장자가 이미지 확장지인지 검증하고,
	 * 확장자를 이용해서 유니크한 파일 이름을 만드는 유틸성 메소드.
	 */
	public static String getUniqueImageFileName(MultipartFile multipartFile) {
		String extension = getFileExtension(multipartFile);
		validateImageExtension(extension);
		return createUniqueFileName(extension);
	}

	private static String getFileExtension(MultipartFile multipartFile) {
		String fileName = getOriginalFilename(multipartFile);
		if (!fileName.contains(DOT)) {
			return "";
		}
		int pos = fileName.lastIndexOf(DOT);
		return fileName.substring(pos + 1);
	}

	private static String getOriginalFilename(MultipartFile multipartFile) {
		String originalFileName = multipartFile.getOriginalFilename();
		if (originalFileName == null) {
			throw new IllegalArgumentException("파일이 존재하지 않습니다.");
		}
		return originalFileName;
	}

	private static void validateImageExtension(String extension) {
		if (!allowImgExtensions.contains(extension)) {
			throw new IllegalArgumentException(String.format("허용되지 않은 이미지 확장자 (%s) 입니다.", extension));
		}
	}

	private static String createUniqueFileName(String extension) {
		return UUID.randomUUID() + DOT + extension;
	}

	/**
	 * MultipartFile 을 File 로 변환하는 유틸성 메소드.
	 */
	public static File convertToFile(MultipartFile multipartFile, String fileName) {
		try {
			File convertFile = new File(TEMP + fileName);
			if (!convertFile.exists()) {
				convertFile.getParentFile().mkdirs();
			}
			return createNewFile(convertFile, multipartFile);
		} catch (IOException e) {
			throw new IllegalArgumentException(String.format("MultipartFile 로 File 을 생성하던 중 에러가 발생하였습니다. (%s)", e));
		}
	}

	private static File createNewFile(File convertFile, MultipartFile multipartFile) throws IOException {
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(multipartFile.getBytes());
			}
			return convertFile;
		}
		throw new IOException("새로운 파일을 생성하지 못했습니다.");
	}

	/**
	 * 임시 파일을 삭제하는 유틸성 메소드.
	 */
	public static void removeTempFile(File targetFile) {
		if (targetFile.delete()) {
			return;
		}
		log.info("파일이 삭제되지 못했습니다. {}", targetFile.getName());
	}

}
