package com.month.service.accreditation;

import com.month.domain.accreditation.Accreditation;
import com.month.domain.accreditation.repository.AccreditationRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AccreditationServiceUtils {

    static void findByChallengeUuidAndDateAndMemberId(AccreditationRepository accreditationRepository, Long memberId, String challengeUuid) {
        Accreditation accreditation = accreditationRepository.findByChallengeUuidAndDateAndMemberId(challengeUuid, LocalDate.now(), memberId);
        if (accreditation != null) {
            throw new IllegalArgumentException(String.format("해당 멤버 (%s) 는 이미 챌린지 (%s)의 인증을 완료했습니다.", memberId, challengeUuid));
        }
    }

}
