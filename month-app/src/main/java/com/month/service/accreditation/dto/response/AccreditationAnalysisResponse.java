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

    private String firstMember;

    private int fisrtMemberDifferenceDay;

    private List<Participant> participants;

    private int personalPercent;

    private int personalDay;

    private int personalTotalDay;

    private int personalContinuityDay;

    public static AccreditationAnalysisResponse of(Challenge challenge, List<Participant> participants, String firstMember, int differenceDay
            , Participant my, int personalTotalDay, int continuityDay) {
        AccreditationAnalysisResponse accreditationAnalysisResponse = new AccreditationAnalysisResponse();
        accreditationAnalysisResponse.challengeName = challenge.getName();
        accreditationAnalysisResponse.challengeDescription = challenge.getDescription();
        accreditationAnalysisResponse.challengeStartDateTime = challenge.getStartDateTime();
        accreditationAnalysisResponse.challengeEndDateTime = challenge.getEndDateTime();
        accreditationAnalysisResponse.participants = participants;
        accreditationAnalysisResponse.firstMember = firstMember;
        accreditationAnalysisResponse.fisrtMemberDifferenceDay = differenceDay;
        accreditationAnalysisResponse.personalPercent = my.getPercent();
        accreditationAnalysisResponse.personalDay = my.getDay();
        accreditationAnalysisResponse.personalTotalDay = personalTotalDay;
        accreditationAnalysisResponse.personalContinuityDay = continuityDay;
        return accreditationAnalysisResponse;
    }
}
