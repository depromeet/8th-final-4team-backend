package com.month.domain.accreditation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Accreditation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memo;

    private String imageUrl;

    private Long memberId;

    private String challengeUuid;

    private LocalDate date;

    private LocalTime time;

    @Builder
    public Accreditation(String memo, String image, Long memberId, String uuid) {
        this.memo = memo;
        this.imageUrl = image;
        this.memberId = memberId;
        this.challengeUuid = uuid;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }
}