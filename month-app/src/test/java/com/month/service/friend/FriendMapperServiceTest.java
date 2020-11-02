package com.month.service.friend;

import com.month.domain.friend.FriendMapper;
import com.month.domain.friend.FriendMapperCreator;
import com.month.domain.friend.FriendMapperRepository;
import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.MemberSetupTest;
import com.month.service.friend.dto.request.CreateFriendMapperRequest;
import com.month.service.friend.dto.request.UpdateFriendFavoriteRequest;
import com.month.service.friend.dto.response.FriendMemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
	void 친구요청을_하면_나의_친구목록에_추가되고_기본적으로_즐겨찾기_되지_않은_상태가_된다() {
		// given
		String email = "friend@email.com";
		Member friend = memberRepository.save(MemberCreator.create(email));

		CreateFriendMapperRequest request = CreateFriendMapperRequest.testInstance(email);

		// when
		friendMapperService.createFriend(request, memberId);

		// then
		List<FriendMapper> friendMappers = friendMapperRepository.findAll();
		assertThat(friendMappers).hasSize(1);
		assertFriendMapper(friendMappers.get(0), memberId, friend.getId(), false);
	}

	private void assertFriendMapper(FriendMapper friendMapper, Long memberId, Long targetMemberId, boolean isFavorite) {
		assertThat(friendMapper.getMemberId()).isEqualTo(memberId);
		assertThat(friendMapper.getTargetMemberId()).isEqualTo(targetMemberId);
		assertThat(friendMapper.isFavorite()).isEqualTo(isFavorite);
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

	@MethodSource("sources_retrieve_friend_list_sort_by_name_desc")
	@ParameterizedTest
	void 나의_친구를_이름_오름차순으로_정렬해서_보여준다(String firstName, String secondName) {
		// given
		Member friend1 = MemberCreator.create("friend1@email.com", firstName);
		Member friend2 = MemberCreator.create("friend2@email.com", secondName);
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

	// 친구 목록에 보이는 이름 오름차순 우선순위: 숫자 > 영대문자 > 영소문자 > 한글
	private static Stream<Arguments> sources_retrieve_friend_list_sort_by_name_desc() {
		return Stream.of(
				Arguments.of("111", "AAA"),
				Arguments.of("AAA", "aaa"),
				Arguments.of("aaa", "가가가"),
				Arguments.of("가가가", "나나나"),
				Arguments.of("aaa", "zzz"),
				Arguments.of("111", "222")
		);
	}

	@MethodSource("sources_retrieve_friend_list_sort_by_name_asc")
	@ParameterizedTest
	void 나의_친구를_이름_내림차순으로_정렬해서_보여준다(String firstName, String secondName) {
		// given
		Member friend1 = MemberCreator.create("friend1@email.com", firstName);
		Member friend2 = MemberCreator.create("friend2@email.com", secondName);
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

	private static Stream<Arguments> sources_retrieve_friend_list_sort_by_name_asc() {
		return Stream.of(
				Arguments.of("111", "aaa"),
				Arguments.of("aaa", "가가가"),
				Arguments.of("가가가", "나나나"),
				Arguments.of("AAA", "aaa"),
				Arguments.of("aaa", "zzz"),
				Arguments.of("111", "222")
		);
	}

	@Test
	void 나의_친구를_친구_추가한_날짜가_빠른순으로_정렬해서_보여준다() {
		// given
		Member second = MemberCreator.create("friend2@email.com");
		Member first = MemberCreator.create("friend1@email.com");
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
		Member second = MemberCreator.create("friend2@email.com");
		Member first = MemberCreator.create("friend1@email.com");
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

	@Test
	void 등록된_친구를_즐겨찾기한다() {
		// given
		boolean isFavorite = true;
		Member friend = memberRepository.save(MemberCreator.create("friend@email.com"));

		friendMapperRepository.save(FriendMapperCreator.create(memberId, friend.getId()));

		UpdateFriendFavoriteRequest request = UpdateFriendFavoriteRequest.testBuilder()
				.friendMemberId(friend.getId())
				.isFavorite(isFavorite)
				.build();

		// when
		friendMapperService.updateFriendFavorite(request, memberId);

		// then
		List<FriendMapper> friendMappers = friendMapperRepository.findAll();
		assertThat(friendMappers).hasSize(1);
		assertFriendMapper(friendMappers.get(0), memberId, friend.getId(), isFavorite);
	}

	@Test
	void 즐겨찾기_되어있는_친구를_즐겨찾기_해제한다() {
		// given
		boolean isFavorite = false;
		Member friend = memberRepository.save(MemberCreator.create("friend@email.com"));

		friendMapperRepository.save(FriendMapperCreator.create(memberId, friend.getId(), true));

		UpdateFriendFavoriteRequest request = UpdateFriendFavoriteRequest.testBuilder()
				.friendMemberId(friend.getId())
				.isFavorite(isFavorite)
				.build();

		// when
		friendMapperService.updateFriendFavorite(request, memberId);

		// then
		List<FriendMapper> friendMappers = friendMapperRepository.findAll();
		assertThat(friendMappers).hasSize(1);
		assertFriendMapper(friendMappers.get(0), memberId, friend.getId(), isFavorite);
	}

	@Test
	void 친구등록된_친구를_친구목록에서_삭제한다() {
		// given
		Member friend = memberRepository.save(MemberCreator.create("friend@email.com"));

		friendMapperRepository.save(FriendMapperCreator.create(memberId, friend.getId(), true));

		// when
		friendMapperService.deleteFriendMapper(friend.getId(), memberId);

		// then
		List<FriendMapper> friendMappers = friendMapperRepository.findAll();
		assertThat(friendMappers).isEmpty();
	}

	@Test
	void 친구등록되지_않은_친구를_친구목록에서_삭제하면_에러발생() {
		// given
		Member notFriend = memberRepository.save(MemberCreator.create("friend@email.com"));

		// when & then
		assertThatThrownBy(() -> {
			friendMapperService.deleteFriendMapper(notFriend.getId(), memberId);
		});
	}

}