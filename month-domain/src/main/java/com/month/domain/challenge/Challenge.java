package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
import com.month.domain.common.DateTimeInterval;
import com.month.domain.common.Uuid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Challenge extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Uuid uuid;

	@Column(nullable = false)
	private String name;

	private String description;

	private int membersCount;

	@Embedded
	private DateTimeInterval dateTimeInterval;

	@Embedded
	private InvitationKey invitationKey;

	@Enumerated(EnumType.STRING)
	private CertifyType certifyType;

	@OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChallengeMemberMapper> memberMappers = new ArrayList<>();

	@Builder
	private Challenge(String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, CertifyType certifyType) {
		this.uuid = Uuid.newInstance();
		this.name = name;
		this.description = description;
		this.membersCount = 0;
		this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
		this.certifyType = certifyType;
	}

	public static Challenge of(String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, CertifyType certifyType) {
		return Challenge.builder()
				.name(name)
				.description(description)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.certifyType(certifyType)
				.build();
	}

	public LocalDateTime getStartDateTime() {
		return this.dateTimeInterval.getStartDateTime();
	}

	public LocalDateTime getEndDateTime() {
		return this.dateTimeInterval.getEndDateTime();
	}

	public String getUuid() {
		return this.uuid.getUuid();
	}

	public String getInvitationKey() {
		return this.invitationKey.getInvitationKey();
	}

	public void addCreator(Long memberId) {
		if (isMemberInChallenge(memberId)) {
			throw new IllegalArgumentException(String.format("멤버 (%s) 는 챌린지 (%s)에 이미 참여하고 있습니다", memberId, this.uuid));
		}
		this.memberMappers.add(ChallengeMemberMapper.creator(this, memberId));
		this.membersCount++;
	}

	public void addParticipator(Long memberId) {
		if (isMemberInChallenge(memberId)) {
			throw new IllegalArgumentException(String.format("멤버 (%s) 는 챌린지 (%s)에 이미 참여하고 있습니다", memberId, this.uuid));
		}
		this.memberMappers.add(ChallengeMemberMapper.participator(this, memberId));
		this.membersCount++;
	}

	private boolean isCreator(Long memberId) {
		return this.memberMappers.stream()
				.anyMatch(memberMapper -> memberMapper.isCreator(memberId));
	}

	private boolean isParticipator(Long memberId) {
		return this.memberMappers.stream()
				.anyMatch(memberMapper -> memberMapper.isParticipator(memberId));
	}

	private boolean isMemberInChallenge(Long memberId) {
		return isCreator(memberId) || isParticipator(memberId);
	}

	public void validateMemberInChallenge(Long memberId) {
		if (!isMemberInChallenge(memberId)) {
			throw new IllegalArgumentException(String.format("멤버 (%s) 는 챌린지 (%s) 에 참가하고 있지 않습니다.", memberId, this.uuid));
		}
	}

	public void createNewInvitationKey(Long memberId) {
		if (!isMemberInChallenge(memberId)) {
			throw new IllegalArgumentException(String.format("챌린지 (%s) 에 참가하고 있는 멤버만이 초대키를 생성할 수 있습니다. 현재 멤버: %s", this.uuid, memberId));
		}
		this.invitationKey = InvitationKey.newInstance(getUuid(), memberId);
	}

	public void validateNotExpiredInvitationKey() {
		if (this.invitationKey.getExpireDateTime().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException(String.format("초대키 (%s) 가 만료 (%s) 되었습니다. 재 발급 받아주세요.",
					this.invitationKey.getInvitationKey(), this.invitationKey.getExpireDateTime()));
		}
	}

	public List<Long> getMembersInChallenge() {
		return this.memberMappers.stream()
				.map(ChallengeMemberMapper::getMemberId)
				.collect(Collectors.toList());
	}

}
