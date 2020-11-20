package com.month.service.challenge.dto.request;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeType;
import com.month.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateNewChallengeRequest {

	@NotBlank
	private String name;

	@NotNull
	private ChallengeType type;

	@NotBlank
	private String color;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;

	@NotNull
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
		return Challenge.newInstance(name, type, color,
				LocalDateTimeUtils.convertToMinOfLocalDateTime(startDate),
				LocalDateTimeUtils.convertToMaxOfLocalDateTime(endDate));
	}

}
