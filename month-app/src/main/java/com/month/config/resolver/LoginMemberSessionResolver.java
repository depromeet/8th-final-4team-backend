package com.month.config.resolver;

import com.month.type.MemberSession;
import com.month.type.SessionConstants;
import com.month.utils.HeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginMemberSessionResolver implements HandlerMethodArgumentResolver {

	private final static String BEARER_TOKEN = "Bearer ";

	private final SessionRepository sessionRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasAnnotation = parameter.getParameterAnnotation(LoginMember.class) != null;
		boolean isMatchType = parameter.getParameterType().equals(MemberSession.class);
		return hasAnnotation && isMatchType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
		Session session = extractSessionFromHeader(header);
		return session.getAttribute(SessionConstants.LOGIN_SESSION);
	}

	private Session extractSessionFromHeader(String header) {
		HeaderUtils.validateAvailableHeader(header);
		Session session = sessionRepository.getSession(header.split(BEARER_TOKEN)[1]);
		if (session == null) {
			throw new IllegalArgumentException(String.format("잘못된 세션입니다 (%s)", header));
		}
		return session;
	}

}