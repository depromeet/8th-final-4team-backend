package com.month.controller.auth;

import com.month.controller.ApiResponse;
import com.month.service.auth.AuthService;
import com.month.service.auth.dto.request.AuthRequest;
import com.month.type.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.month.type.SessionConstants.LOGIN_SESSION;

@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;
	private final HttpSession httpSession;

	@PostMapping("/api/v1/auth")
	public ApiResponse<String> handleAuthentication(@Valid @RequestBody AuthRequest request) {
		Long memberId = authService.handleAuthentication(request);
		httpSession.setAttribute(LOGIN_SESSION, MemberSession.of(memberId));
		return ApiResponse.of(httpSession.getId());
	}

	@PostMapping("/api/v1/logout")
	public ApiResponse<String> handleLogout() {
		httpSession.removeAttribute(LOGIN_SESSION);
		return ApiResponse.OK;
	}
	
}
