package com.month.service.accreditation;

import com.month.domain.accreditation.Accreditation;
import com.month.domain.accreditation.repository.AccreditationRepository;
import com.month.service.accreditation.dto.request.AccreditationRequest;
import com.month.domain.member.MemberRepository;
import com.month.service.accreditation.dto.response.AccreditationResponse;
import com.month.service.member.MemberServiceUtils;
import com.month.service.upload.UploadService;
import com.month.type.UploadType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccreditationService {

    private final AccreditationRepository accreditationRepository;

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

}