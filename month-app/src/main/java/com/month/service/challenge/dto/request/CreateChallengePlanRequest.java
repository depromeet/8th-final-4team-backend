package com.month.service.challenge.dto.request;

import com.month.domain.challenge.ChallengePlan;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CreateChallengePlanRequest {

	@NotBlank
	private String name;

	private String description;

	@NotBlank
	private String color;

	@NotNull
	private int period;

	@NotNull
	private int maxMembersCount;

	public ChallengePlan toEntity() {
		return ChallengePlan.newInstance(name, description, color, period, maxMembersCount);
	}

	@Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
	public CreateChallengePlanRequest(String name, String description, String color, int period, int maxMembersCount) {
		this.name = name;
		this.description = description;
		this.color = color;
		this.period = period;
		this.maxMembersCount = maxMembersCount;
	}

}
