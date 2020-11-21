package com.month.service.accreditation.dto.response;

import com.month.domain.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Participant {

    private String name;

    private String photoUrl;

    private int achievePeriod;

    private int totalPeriod;

    public static Participant of(Member member, Long total, int achieve) {
        Participant participant = new Participant();
        participant.name = member.getName();
        participant.photoUrl = member.getPhotoUrl();
        participant.achievePeriod = achieve;
        participant.totalPeriod = total.intValue();
        return participant;
    }

    public static Participant maxParticipant(List<Participant> participantList) {
        Participant participant = participantList.stream()
                .max(Comparator.comparing(Participant::getAchievePeriod))
                .get();
        return participant;
    }

}
