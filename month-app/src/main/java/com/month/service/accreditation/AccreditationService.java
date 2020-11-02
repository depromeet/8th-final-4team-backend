package com.month.service.accreditation;

import com.month.domain.accreditation.Accreditation;
import com.month.domain.accreditation.repository.AccreditationRepository;
import com.month.service.accreditation.dto.request.AccreditationRequest;
import com.month.service.upload.UploadService;
import com.month.type.UploadType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccreditationService {

    private final AccreditationRepository accreditationRepository;

    private final UploadService uploadService;

    public void saveAccreditation(Long memberId, AccreditationRequest request) {
        String url = "";
        if (request.getImage() != null)
            url = uploadService.upload(request.getImage(), UploadType.CHALLENGE.getDirectory());

        Accreditation accreditation = new Accreditation(request.getMemo(), url, memberId, request.getChallengeUuid());
        accreditationRepository.save(accreditation);
    }

}