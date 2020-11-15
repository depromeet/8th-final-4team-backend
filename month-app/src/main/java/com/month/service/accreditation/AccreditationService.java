package com.month.service.accreditation;

import com.month.domain.accreditation.Accreditation;
import com.month.domain.accreditation.repository.AccreditationRepository;
import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.common.Uuid;
import com.month.service.accreditation.dto.request.AccreditationRequest;
import com.month.domain.member.MemberRepository;
import com.month.service.accreditation.dto.response.AccreditationAnalysisResponse;
import com.month.service.accreditation.dto.response.AccreditationResponse;
import com.month.service.accreditation.dto.response.Participant;
import com.month.service.member.MemberServiceUtils;
import com.month.service.upload.UploadService;
import com.month.type.UploadType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccreditationService {

    private final AccreditationRepository accreditationRepository;

    private final ChallengeRepository challengeRepository;

    private final MemberRepository memberRepository;

    private final UploadService uploadService;

    @Transactional
    public void saveAccreditation(Long memberId, AccreditationRequest request) {
        AccreditationServiceUtils.findByChallengeUuidAndDateAndMemberId(accreditationRepository, memberId, request.getChallengeUuid());

        String url = uploadService.upload(request.getImage(), UploadType.CHALLENGE.getDirectory());
        Accreditation accreditation = new Accreditation(request.getMemo(), url, memberId, request.getChallengeUuid());
        accreditationRepository.save(accreditation);
    }

    @Transactional(readOnly = true)
    public List<AccreditationResponse> getAccreditationList(Long memberId, String challengeUuid, LocalDate date) {
        List<Accreditation> accreditationList = accreditationRepository.findAllByChallengeUuidAndDate(challengeUuid, date);
        return accreditationList.stream()
                .map(accreditation -> AccreditationResponse.of(accreditation, MemberServiceUtils.findMemberById(memberRepository, accreditation.getMemberId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public void getAccreditationCheck(Long memberId, String challengeUuid) {
        AccreditationServiceUtils.findByChallengeUuidAndDateAndMemberId(accreditationRepository, memberId, challengeUuid);
    }

    @Transactional(readOnly = true)
    public AccreditationAnalysisResponse getAccreditationAnalysis(Long memberId, String challengeUuid) {
        Uuid uuid = new Uuid(challengeUuid);
        Challenge challenge = challengeRepository.findByUuid(uuid);
        Long day = ChronoUnit.DAYS.between(challenge.getStartDateTime(), challenge.getEndDateTime());

        List<Participant> participantList = challenge.getMemberIds().stream()
                .map(members -> Participant.of(MemberServiceUtils.findMemberById(memberRepository, members), day, accreditationRepository.findAllByChallengeUuidAndMemberId(challengeUuid, members).size()))
                .collect(Collectors.toList());

        Participant firstParticipant = Participant.maxParticipant(participantList);
        Participant MyParticipant = Participant.of(MemberServiceUtils.findMemberById(memberRepository, memberId), day, accreditationRepository.findAllByChallengeUuidAndMemberId(challengeUuid, memberId).size());
        int differenceDay = firstParticipant.getDay() - MyParticipant.getDay();

        return AccreditationAnalysisResponse.of(challenge, participantList, firstParticipant.getName(), differenceDay, MyParticipant, day.intValue(), continuityDay(memberId, challengeUuid));
    }

    private int continuityDay(Long memberId, String challengeUuid) {
        List<Accreditation> accreditationList = accreditationRepository.findAllByChallengeUuidAndMemberId(challengeUuid, memberId);
        int continuity = 1;
        int max_continuity = 1;
        for (int i = 0; i < accreditationList.size() - 1; i++) {
            Long differenceDay = ChronoUnit.DAYS.between(accreditationList.get(i).getDate(), accreditationList.get(i + 1).getDate());
            if (differenceDay.intValue() == 1)
                continuity++;
            else {
                if (continuity > max_continuity) {
                    max_continuity = continuity;
                }
                continuity = 0;
            }
        }
        return max_continuity;
    }

}