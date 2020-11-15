package com.month.service.accreditation.dto.response;

import com.month.domain.challenge.Challenge;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AccreditationAnalysisResponse {

    private String challengeName;

    private String challengeDescription;

    private LocalDateTime challengeStartDateTime;

    private LocalDateTime challengeEndDateTime;

    private String firstMemberName;

    private int firstMemberDifferenceDay;

    private List<Participant> participants;

    private int personalAchievePercent;

    private int personalAchievePeriod;

    private int personalTotalPeriod;

    private int personalContinuityPeriod;

    public static AccreditationAnalysisResponse of(Challenge challenge, List<Participant> participants, Participant firstMember, Participant my, int totalPeriod, int continuityDay) {
        AccreditationAnalysisResponse accreditationAnalysisResponse = new AccreditationAnalysisResponse();
        accreditationAnalysisResponse.challengeName = challenge.getName();
        accreditationAnalysisResponse.challengeDescription = challenge.getDescription();
        accreditationAnalysisResponse.challengeStartDateTime = challenge.getStartDateTime();
        accreditationAnalysisResponse.challengeEndDateTime = challenge.getEndDateTime();
        accreditationAnalysisResponse.participants = participants;
        accreditationAnalysisResponse.firstMemberName = firstMember.getName();
        accreditationAnalysisResponse.firstMemberDifferenceDay = firstMember.getAchievePeriod() - my.getAchievePeriod();
        accreditationAnalysisResponse.personalAchievePercent = 100 * my.getAchievePeriod() / totalPeriod;
        accreditationAnalysisResponse.personalAchievePeriod = my.getAchievePeriod();
        accreditationAnalysisResponse.personalTotalPeriod = totalPeriod;
        accreditationAnalysisResponse.personalContinuityPeriod = continuityDay;
        return accreditationAnalysisResponse;
    }
}
