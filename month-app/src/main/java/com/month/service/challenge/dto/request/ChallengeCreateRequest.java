package com.month.service.challenge.dto.request;

import com.month.domain.challenge.CertifyType;
import com.month.domain.challenge.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChallengeCreateRequest {

	private String name;

	private String description;

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	private CertifyType certifyType;

	@Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
	public ChallengeCreateRequest(String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, CertifyType certifyType) {
		this.name = name;
		this.description = description;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.certifyType = certifyType;
	}

	public Challenge toEntity() {
		return Challenge.of(name, description, startDateTime, endDateTime, certifyType);
	}

}
