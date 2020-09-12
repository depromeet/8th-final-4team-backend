package com.month.service.member;

import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.service.member.dto.request.MemberUpdateInfoRequest;
import com.month.service.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public MemberInfoResponse getMemberInfo(Long memberId) {
		Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
		return MemberInfoResponse.of(member);
	}

	@Transactional
	public MemberInfoResponse updateMemberInfo(MemberUpdateInfoRequest request, Long memberId) {
		Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
		member.updateInfo(request.getName(), request.getPhotoUrl());
		return MemberInfoResponse.of(member);
	}

}
