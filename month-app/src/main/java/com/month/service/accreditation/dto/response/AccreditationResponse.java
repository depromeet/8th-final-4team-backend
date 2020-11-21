package com.month.service.accreditation.dto.response;

import com.month.domain.accreditation.Accreditation;
import com.month.domain.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class AccreditationResponse {

    private String memberName;

    private String memberPhotoUrl;

    private LocalTime time;

    private String imageUrl;

    private String memo;

    public static AccreditationResponse of(Accreditation accreditation, Member member) {
        AccreditationResponse accreditationResponse = new AccreditationResponse();
        accreditationResponse.memberName = member.getName();
        accreditationResponse.memberPhotoUrl = member.getPhotoUrl();
        accreditationResponse.time = accreditation.getTime();
        accreditationResponse.imageUrl = accreditation.getImageUrl();
        accreditationResponse.memo = accreditation.getMemo();
        return accreditationResponse;
    }

}
