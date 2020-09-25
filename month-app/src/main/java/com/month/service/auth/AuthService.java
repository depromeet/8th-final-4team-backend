package com.month.service.auth;

import com.month.external.firebase.FirebaseUtils;
import com.month.external.firebase.dto.CustomFirebaseToken;
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
	private final FirebaseUtils firebaseUtils;

	@Transactional
	public Long handleAuthentication(AuthRequest request) {
		CustomFirebaseToken firebaseToken = firebaseUtils.getDecodedToken(request.getIdToken());
		Member findMember = memberRepository.findMemberByUid(firebaseToken.getUid());
		if (findMember == null) {
			Member newMember = memberRepository.save(firebaseToken.toEntity());
			return newMember.getId();
		}
		return findMember.getId();
	}

}
