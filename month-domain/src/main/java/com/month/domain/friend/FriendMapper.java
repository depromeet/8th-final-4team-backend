package com.month.domain.friend;

import com.month.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FriendMapper extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private Long targetMemberId;

	private boolean isFavorite;

	@Builder
	public FriendMapper(Long memberId, Long targetMemberId) {
		this.memberId = memberId;
		this.targetMemberId = targetMemberId;
		this.isFavorite = false;
	}

	public static FriendMapper newInstance(Long memberId, Long targetMemberId) {
		return FriendMapper.builder()
				.memberId(memberId)
				.targetMemberId(targetMemberId)
				.build();
	}

}
