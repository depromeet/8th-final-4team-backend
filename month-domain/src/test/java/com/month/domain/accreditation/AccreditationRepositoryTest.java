package com.month.domain.accreditation;

import com.month.domain.accreditation.repository.AccreditationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccreditationRepositoryTest {

	@Autowired
	private AccreditationRepository accreditationRepository;

	@Test
	void test_findAllByChallengeUuid() {
		// given
		Accreditation accreditation1 = AccreditationCreator.create(1L, "uuid1");
		Accreditation accreditation2 = AccreditationCreator.create(1L, "uuid2");
		accreditationRepository.saveAll(Arrays.asList(accreditation1, accreditation2));

		// when
		List<String> uuidList = Arrays.asList(accreditation1.getChallengeUuid(), accreditation2.getChallengeUuid());
		List<Accreditation> accreditationList = accreditationRepository.findAllByChallengeUuidList(uuidList);

		// then
		assertThat(accreditationList).hasSize(2);
	}

}
