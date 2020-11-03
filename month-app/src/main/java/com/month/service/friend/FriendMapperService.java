package com.month.service.friend;

import com.month.domain.friend.FriendMapper;
import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.domain.friend.FriendMapperRepository;
import com.month.service.friend.dto.request.CreateFriendMapperRequest;
import com.month.service.friend.dto.request.UpdateFriendFavoriteRequest;
import com.month.service.friend.dto.response.FriendMemberInfoResponse;
import com.month.service.member.MemberServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FriendMapperService {

	private final MemberRepository memberRepository;
	private final FriendMapperRepository friendMapperRepository;

	@Transactional
	public void createFriend(CreateFriendMapperRequest request, Long memberId) {
		Member targetMember = MemberServiceUtils.findMemberByEmail(memberRepository, request.getEmail());
		if (targetMember.isSameMember(memberId)) {
			throw new IllegalArgumentException(String.format("자기 자신 (%s)은 친구 등록 할 수 없습니다", memberId));
		}
		FriendMapperServiceUtils.validateNonFriendMapper(friendMapperRepository, memberId, targetMember.getId());
		friendMapperRepository.save(FriendMapper.newInstance(memberId, targetMember.getId()));
		// TODO 상대방이 나를 친구 추가 하지 않았을 경우에, Notification 가는 기능?
	}

	@Transactional(readOnly = true)
	public List<FriendMemberInfoResponse> retrieveMyFriendsInfo(FriendListSortType sortBy, Long memberId) {
		List<FriendMapper> friendMappers = friendMapperRepository.findAllByMemberId(memberId);
		Map<Long, FriendMapper> friendMapperMaps = friendMappers.stream()
				.collect(Collectors.toMap(FriendMapper::getTargetMemberId, friendMapper -> friendMapper));

		List<Member> friends = memberRepository.findAllById(getFriendIds(friendMappers));

		return friends.stream()
				.map(friend -> FriendMemberInfoResponse.of(friend, friendMapperMaps.get(friend.getId())))
				.sorted(sortBy.getComparator())
				.collect(Collectors.toList());
	}

	private List<Long> getFriendIds(List<FriendMapper> friendMappers) {
		return friendMappers.stream()
				.map(FriendMapper::getTargetMemberId)
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

}
