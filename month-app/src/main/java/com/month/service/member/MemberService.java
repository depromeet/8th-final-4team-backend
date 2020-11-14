package com.month.service.member;

import com.month.domain.accreditation.Accreditation;
import com.month.domain.accreditation.repository.AccreditationRepository;
import com.month.domain.challenge.ChallengeCollection;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.service.member.dto.request.MemberUpdateInfoRequest;
import com.month.service.member.dto.response.MemberDetailInfoResponse;
import com.month.service.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final ChallengeRepository challengeRepository;
	private final AccreditationRepository accreditationRepository;

	@Transactional(readOnly = true)
	public MemberDetailInfoResponse getMemberInfo(Long memberId) {
		Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
		ChallengeCollection collection = ChallengeCollection.of(challengeRepository.findChallengesByMemberId(memberId));
		List<Accreditation> accreditationList = accreditationRepository.findAllByChallengeUuidList(collection.getChallengesUuidList());
		return MemberDetailInfoResponse.of(member, collection.getChallengesCount(),
				collection.getAchieveRateOfChallenge(accreditationList.size()));
	}

	@Transactional
	public MemberInfoResponse updateMemberInfo(MemberUpdateInfoRequest request, Long memberId) {
		Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
		member.updateInfo(request.getName(), request.getPhotoUrl());
		return MemberInfoResponse.of(member);
	}

}
