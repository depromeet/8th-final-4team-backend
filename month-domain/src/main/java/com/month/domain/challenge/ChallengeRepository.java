package com.month.domain.challenge;

import com.month.domain.challenge.repository.ChallengeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryCustom {

}
