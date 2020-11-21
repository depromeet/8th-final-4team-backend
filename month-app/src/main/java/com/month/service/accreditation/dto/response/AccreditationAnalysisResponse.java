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

    private LocalDateTime challengeStartDateTime;

    private LocalDateTime challengeEndDateTime;

    private String firstMemberName;

    private int firstMemberDifferenceDay;

    private List<Participant> participants;

    private int personalAchievePercent;

    private int personalAchievePeriod;

    private int personalTotalPeriod;

    private int personalContinuityPeriod;

    public static AccreditationAnalysisResponse of(Challenge challenge, List<Participant> participantList, Participant my, int totalPeriod, int continuityDay) {
        AccreditationAnalysisResponse accreditationAnalysisResponse = new AccreditationAnalysisResponse();
        accreditationAnalysisResponse.challengeName = challenge.getName();
        accreditationAnalysisResponse.challengeStartDateTime = challenge.getStartDateTime();
        accreditationAnalysisResponse.challengeEndDateTime = challenge.getEndDateTime();
        accreditationAnalysisResponse.participants = participantList;
        accreditationAnalysisResponse.firstMemberName = participantList.get(0).getName();
        accreditationAnalysisResponse.firstMemberDifferenceDay = participantList.get(0).getAchievePeriod() - my.getAchievePeriod();
        accreditationAnalysisResponse.personalAchievePercent = 100 * my.getAchievePeriod() / totalPeriod;
        accreditationAnalysisResponse.personalAchievePeriod = my.getAchievePeriod();
        accreditationAnalysisResponse.personalTotalPeriod = totalPeriod;
        accreditationAnalysisResponse.personalContinuityPeriod = continuityDay;
        return accreditationAnalysisResponse;
    }
}
