package com.month.domain.challenge;

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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Challenge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Uuid uuid;

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
		this.membersCount = 0;
		this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
		this.name = name;
		this.description = description;
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

	public LocalDateTime getExpireDateTimeOfInvitationKey() {
		return this.invitationKey.getExpireDateTime();
	}

	public void addCreator(Long memberId) {
		this.memberMappers.add(ChallengeMemberMapper.creator(this, memberId));
		this.membersCount++;
	}

	public void addParticipator(Long memberId) {
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

}
