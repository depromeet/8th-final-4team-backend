package com.month.controller.auth;

import com.month.controller.ApiResponse;
import com.month.service.auth.AuthService;
import com.month.service.auth.dto.request.AuthRequest;
import com.month.type.session.MemberSession;
import com.month.utils.HeaderUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.month.type.session.SessionConstants.LOGIN_SESSION;

@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;
	private final HttpSession httpSession;

	private final SessionRepository sessionRepository;

	@ApiOperation("회원가입 or 로그인을 진행하는 API")
	@PostMapping("/api/v1/auth")
	public ApiResponse<String> handleAuthentication(@Valid @RequestBody AuthRequest request) {
		Long memberId = authService.handleAuthentication(request);
		httpSession.setAttribute(LOGIN_SESSION, MemberSession.of(memberId));
		return ApiResponse.of(httpSession.getId());
	}

	@ApiOperation("로그아웃을 진행하는 API")
	@PostMapping("/api/v1/logout")
	public ApiResponse<String> handleLogout(@RequestHeader HttpHeaders httpHeaders) {
		String header = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
		HeaderUtils.validateAvailableHeader(header);
		sessionRepository.delete(header.split(HeaderUtils.BEARER_TOKEN)[1]);
		return ApiResponse.OK;
	}

}
