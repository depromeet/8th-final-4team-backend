package com.month.service.auth;

import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.service.auth.dto.request.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final MemberRepository memberRepository;

	@Transactional
	public Long handleAuthentication(AuthRequest request) {
		// TODO idToken	이 정말로 Firebase 에서 발급하였는지 검증과정 필요. (이메일과 대응해서)
		Member member = memberRepository.findMemberByTokenAndEmail(request.getIdToken(), request.getEmail());
		if (member == null) {
			member = memberRepository.save(request.toEntity());
		}
		return member.getId();
	}

}
