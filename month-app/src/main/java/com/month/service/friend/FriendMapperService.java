package com.month.service.friend;

import com.month.domain.friend.FriendMapper;
import com.month.domain.friend.FriendMapperCollection;
import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.domain.friend.FriendMapperRepository;
import com.month.domainservice.AchievementRateDomainService;
import com.month.exception.NotAllowedException;
import com.month.service.friend.dto.request.CreateFriendMapperRequest;
import com.month.service.friend.dto.request.RetrieveFriendDetailRequest;
import com.month.service.friend.dto.request.UpdateFriendFavoriteRequest;
import com.month.service.friend.dto.response.FriendMemberDetailInfoResponse;
import com.month.service.friend.dto.response.FriendMemberInfoResponse;
import com.month.service.member.MemberServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.month.exception.type.ExceptionDescriptionType.REGISTER_FRIEND;

@RequiredArgsConstructor
@Service
public class FriendMapperService {

	private final MemberRepository memberRepository;
	private final FriendMapperRepository friendMapperRepository;
	private final AchievementRateDomainService achievementRateDomainService;

	@Transactional
	public void createFriend(CreateFriendMapperRequest request, Long memberId) {
		Member targetMember = MemberServiceUtils.findMemberByEmail(memberRepository, request.getEmail());
		if (targetMember.isSameMember(memberId)) {
			throw new NotAllowedException(String.format("자기 자신 (%s)은 친구 등록 할 수 없습니다", memberId), REGISTER_FRIEND);
		}
		FriendMapperServiceUtils.validateNonFriendMapper(friendMapperRepository, memberId, targetMember.getId());
		friendMapperRepository.save(FriendMapper.newInstance(memberId, targetMember.getId()));
		// TODO 상대방이 나를 친구 추가 하지 않았을 경우에, Notification 가는 기능?
	}

	@Transactional(readOnly = true)
	public List<FriendMemberInfoResponse> retrieveMyFriendsInfo(FriendListSortType sortBy, Long memberId) {
		FriendMapperCollection collection = FriendMapperCollection.of(friendMapperRepository.findAllByMemberId(memberId));
		List<Member> friends = memberRepository.findAllById(collection.getFriendsIds());
		return friends.stream()
				.map(friend -> FriendMemberInfoResponse.of(friend, collection.getFriendMapperByFriendId(friend.getId())))
				.sorted(sortBy.getComparator())
				.collect(Collectors.toList());
	}

	@Transactional
	public void updateFriendFavorite(UpdateFriendFavoriteRequest request, Long memberId) {
		FriendMapper friendMapper = FriendMapperServiceUtils.findFriendMapper(friendMapperRepository, memberId, request.getFriendMemberId());
		friendMapper.updateFavorite(request.getIsFavorite());
	}

	@Transactional
	public void deleteFriendMapper(Long friendMemberId, Long memberId) {
		FriendMapper friendMapper = FriendMapperServiceUtils.findFriendMapper(friendMapperRepository, memberId, friendMemberId);
		friendMapperRepository.delete(friendMapper);
	}

	@Transactional(readOnly = true)
	public FriendMemberDetailInfoResponse retrieveFriendDetailInfo(RetrieveFriendDetailRequest request, Long memberId) {
		Member friend = MemberServiceUtils.findMemberById(memberRepository, request.getFriendId());
		return FriendMemberDetailInfoResponse.of(friend,
				FriendMapperServiceUtils.findFriendMapper(friendMapperRepository, memberId, friend.getId()),
				achievementRateDomainService.getChallengesCountWithFriend(memberId, friend.getId()),
				achievementRateDomainService.getMemberAchievementRate(friend.getId()).getAchieveChallengeRate());
	}

}
