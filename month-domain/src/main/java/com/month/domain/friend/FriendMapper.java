package com.month.domain.friend;

import com.month.domain.BaseTimeEntity;
import com.month.exception.NotAllowedException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.month.exception.type.ExceptionDescriptionType.REGISTER_FRIEND;

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
	public FriendMapper(Long memberId, Long targetMemberId, boolean isFavorite) {
		validateNotMySelf(memberId, targetMemberId);
		this.memberId = memberId;
		this.targetMemberId = targetMemberId;
		this.isFavorite = isFavorite;
	}

	private void validateNotMySelf(Long memberId, Long targetMemberId) {
		if (memberId.equals(targetMemberId)) {
			throw new NotAllowedException(String.format("자기 자신 (%s)은 친구 등록 할 수 없습니다", memberId), REGISTER_FRIEND);
		}
	}

	public static FriendMapper newInstance(Long memberId, Long targetMemberId) {
		return FriendMapper.builder()
				.memberId(memberId)
				.targetMemberId(targetMemberId)
				.isFavorite(false)
				.build();
	}

	public void updateFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

}
