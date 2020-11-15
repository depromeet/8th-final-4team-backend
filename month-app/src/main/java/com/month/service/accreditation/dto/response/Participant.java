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

    private int percent;

    private int day;

    public static Participant of(Member member, Long day, int myday) {
        Participant participant = new Participant();
        participant.name = member.getName();
        participant.photoUrl = member.getPhotoUrl();
        participant.percent = 100 * myday / day.intValue();
        participant.day = myday;
        return participant;
    }

    public static Participant maxParticipant(List<Participant> participantList) {
        Participant participant = participantList.stream()
                .max(Comparator.comparing(Participant::getDay))
                .get();
        return participant;
    }

}
