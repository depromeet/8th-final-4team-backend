package com.month.service.challenge.dto.request;

import com.month.domain.challenge.ChallengePlan;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CreateChallengePlanRequest {

	@NotBlank
	private String name;

	private String description;

	@NotBlank
	private String color;

	@Min(value = 1)
	private int period;

	@Min(value = 1)
	@Max(value = 4)
	private int maxMembersCount;

	public ChallengePlan toEntity(Long memberId) {
		ChallengePlan challengePlan = ChallengePlan.newInstance(name, description, color, period, maxMembersCount);
		challengePlan.addCreator(memberId);
		return challengePlan;
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
