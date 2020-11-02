package com.month.service.friend;

import com.month.domain.friend.FriendMapper;
import com.month.domain.friend.FriendMapperCreator;
import com.month.domain.friend.FriendMapperRepository;
import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.MemberSetupTest;
import com.month.service.friend.dto.request.CreateFriendMapperRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}