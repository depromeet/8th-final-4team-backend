
package com.month.domain.accreditation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccreditationCreator {

	public static Accreditation create(Long memberId, String challengeUuid) {
		return Accreditation.builder()
				.memo("인증완료!")
				.image("imageUrl")
				.memberId(memberId)
				.uuid(challengeUuid)
				.build();
	}

}