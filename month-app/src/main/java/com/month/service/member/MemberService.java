package com.month.service.member;

import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.domainservice.AchievementRateDomainService;
import com.month.domainservice.dto.response.MemberAchieveRateResponse;
import com.month.service.member.dto.request.UpdateMemberInfoRequest;
import com.month.service.member.dto.response.MemberDetailInfoResponse;
import com.month.service.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final AchievementRateDomainService achievementRateDomainService;

	@Transactional(readOnly = true)
	public MemberDetailInfoResponse getMemberInfo(Long memberId) {
		Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
		MemberAchieveRateResponse achievementRate = achievementRateDomainService.getMemberAchievementRate(member.getId());
		return MemberDetailInfoResponse.of(member, achievementRate.getTotalChallengesCount(), achievementRate.getAchieveChallengeRate());
	}

	@Transactional
	public MemberInfoResponse updateMemberInfo(UpdateMemberInfoRequest request, Long memberId) {
		Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
		member.updateInfo(request.getName(), request.getPhotoUrl());
		return MemberInfoResponse.of(member);
	}

}
