package com.month.service.challenge.dto.request;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateNewChallengeRequest {

	private String name;

	private ChallengeType type;

	private String color;

	private LocalDate startDate;

	private LocalDate endDate;

	private List<Long> friendIds;

	@Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
	public CreateNewChallengeRequest(String name, ChallengeType type, String color, LocalDate startDate, LocalDate endDate, List<Long> friendIds) {
		this.name = name;
		this.type = type;
		this.color = color;
		this.startDate = startDate;
		this.endDate = endDate;
		this.friendIds = friendIds;
	}

	public Challenge toEntity() {
		// TODO 이미 지난 날짜로 설정하지는 않았는지 검증하는 로직 필요.
		return Challenge.newInstance(name, type, color, startDate.atTime(LocalTime.MIN), endDate.atTime(11, 59, 59));
		// LocalDatTimeUtils 만들어서 MIN, MAX 고정시키기
	}

}
