package com.month.domain.accreditation.repository;

import com.month.domain.accreditation.Accreditation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AccreditationRepository extends JpaRepository<Accreditation, Long>, AccreditationRepositoryCustom {
    List<Accreditation> findAllByChallengeUuidAndDate(String challengeUuid, LocalDate date);
    Accreditation findByChallengeUuidAndDateAndMemberId(String challengeUuid, LocalDate date, Long memberId);
    List<Accreditation> findAllByChallengeUuidAndMemberId(String challengeUuid, Long memberId);
}
