package com.month.controller.auth;

import com.month.controller.ApiResponse;
import com.month.service.auth.AuthService;
import com.month.service.auth.dto.request.AuthRequest;
import com.month.service.auth.dto.response.AuthResponse;
import com.month.utils.HeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

	private final SessionRepository sessionRepository;

	@PostMapping("/api/v1/auth")
	public ApiResponse<AuthResponse> handleAuthentication(@Valid @RequestBody AuthRequest request) {
		return ApiResponse.of(authService.handleAuthentication(request));
	}

	@PostMapping("/api/v1/logout")
	public ApiResponse<String> handleLogout(@RequestHeader HttpHeaders httpHeaders) {
		String header = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
		HeaderUtils.validateAvailableHeader(header);
		sessionRepository.delete(header.split(HeaderUtils.BEARER_TOKEN)[1]);
		return ApiResponse.OK;
	}


}
