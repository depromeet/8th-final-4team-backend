package com.month.service.friend;

import com.month.domain.friend.FriendMapper;
import com.month.domain.friend.FriendMapperCreator;
import com.month.domain.friend.FriendMapperRepository;
import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.MemberSetupTest;
import com.month.service.friend.dto.request.CreateFriendMapperRequest;
import com.month.service.friend.dto.response.FriendMemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FriendMapperServiceTest extends MemberSetupTest {

	@Autowired
	private FriendMapperRepository friendMapperRepository;

	@Autowired
	private FriendMapperService friendMapperService;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void cleanUp() {
		friendMapperRepository.deleteAll();
		memberRepository.deleteAll();
	}

	@Test
	void 친구요청을_하면_나의_친구목록에_추가된다() {
		// given
		String email = "friend@email.com";
		Member friend = memberRepository.save(MemberCreator.create(email));

		CreateFriendMapperRequest request = CreateFriendMapperRequest.testInstance(email);

		// when
		friendMapperService.createFriend(request, memberId);

		// then
		List<FriendMapper> friendMappers = friendMapperRepository.findAll();
		assertThat(friendMappers).hasSize(1);
		assertFriendMapper(friendMappers.get(0), memberId, friend.getId());
	}

	private void assertFriendMapper(FriendMapper friendMapper, Long memberId, Long targetMemberId) {
		assertThat(friendMapper.getMemberId()).isEqualTo(memberId);
		assertThat(friendMapper.getTargetMemberId()).isEqualTo(targetMemberId);
	}

	@Test
	void 이미_친구요청이_되어있는데_친구요청을_다시하면_에러_발생한다() {
		// given
		String email = "friend@email.com";
		Member friend = memberRepository.save(MemberCreator.create(email));

		friendMapperRepository.save(FriendMapperCreator.create(memberId, friend.getId()));

		CreateFriendMapperRequest request = CreateFriendMapperRequest.testInstance(email);

		// when & then
		assertThatThrownBy(() -> {
			friendMapperService.createFriend(request, memberId);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 나의_친구를_이름_오름차순으로_정렬해서_보여준다() {
		// given
		Member friend1 = MemberCreator.create("friend1@email.com", "가가가", null, "uid1");
		Member friend2 = MemberCreator.create("friend2@email.com", "나나나", null, "uid2");
		memberRepository.saveAll(Arrays.asList(friend1, friend2));

		friendMapperRepository.saveAll(Arrays.asList(
				FriendMapperCreator.create(memberId, friend1.getId()),
				FriendMapperCreator.create(memberId, friend2.getId())
		));

		// when
		List<FriendMemberInfoResponse> result = friendMapperService.retrieveMyFriendsInfo(FriendListSortType.NAME, memberId);

		// then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getId()).isEqualTo(friend1.getId());
		assertThat(result.get(1).getId()).isEqualTo(friend2.getId());
	}

	@Test
	void 나의_친구를_이름_내림차순으로_정렬해서_보여준다() {
		// given
		Member friend1 = MemberCreator.create("friend1@email.com", "가가가", null, "uid1");
		Member friend2 = MemberCreator.create("friend2@email.com", "나나나", null, "uid2");
		memberRepository.saveAll(Arrays.asList(friend1, friend2));

		friendMapperRepository.saveAll(Arrays.asList(
				FriendMapperCreator.create(memberId, friend1.getId()),
				FriendMapperCreator.create(memberId, friend2.getId())
		));

		// when
		List<FriendMemberInfoResponse> result = friendMapperService.retrieveMyFriendsInfo(FriendListSortType.NAME_REVERSE, memberId);

		// then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getId()).isEqualTo(friend2.getId());
		assertThat(result.get(1).getId()).isEqualTo(friend1.getId());
	}

	@Test
	void 나의_친구를_친구_추가한_날짜가_빠른순으로_정렬해서_보여준다() {
		// given
		Member second = MemberCreator.create("friend2@email.com", "나나나", null, "uid2");
		Member first = MemberCreator.create("friend1@email.com", "가가가", null, "uid1");
		memberRepository.saveAll(Arrays.asList(second, first));

		friendMapperRepository.saveAll(Arrays.asList(
				FriendMapperCreator.create(memberId, first.getId()),
				FriendMapperCreator.create(memberId, second.getId())
		));

		// when
		List<FriendMemberInfoResponse> result = friendMapperService.retrieveMyFriendsInfo(FriendListSortType.CREATED, memberId);

		// then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getId()).isEqualTo(first.getId());
		assertThat(result.get(1).getId()).isEqualTo(second.getId());
	}

	@Test
	void 나의_친구를_친구_추가한_날짜가_늦은순으로_정렬해서_보여준다() {
		// given
		Member second = MemberCreator.create("friend2@email.com", "나나나", null, "uid2");
		Member first = MemberCreator.create("friend1@email.com", "가가가", null, "uid1");
		memberRepository.saveAll(Arrays.asList(second, first));

		friendMapperRepository.saveAll(Arrays.asList(
				FriendMapperCreator.create(memberId, first.getId()),
				FriendMapperCreator.create(memberId, second.getId())
		));

		// when
		List<FriendMemberInfoResponse> result = friendMapperService.retrieveMyFriendsInfo(FriendListSortType.CREATED_REVERSE, memberId);

		// then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getId()).isEqualTo(second.getId());
		assertThat(result.get(1).getId()).isEqualTo(first.getId());
	}

	@Test
	void 친구_추가_아무도_안되있을때_널이아닌_빈_리스트_반환한다() {
		// when
		List<FriendMemberInfoResponse> result = friendMapperService.retrieveMyFriendsInfo(FriendListSortType.CREATED_REVERSE, memberId);

		// then
		assertThat(result).isEmpty();
		assertThat(result).isNotNull();
	}

}