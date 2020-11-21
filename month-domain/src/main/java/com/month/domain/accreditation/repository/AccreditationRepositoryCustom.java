package com.month.domain.accreditation.repository;

import com.month.domain.accreditation.Accreditation;

import java.util.List;

public interface AccreditationRepositoryCustom {

	List<Accreditation> findAllByChallengeUuidList(List<String> challengesUuidList);

}
