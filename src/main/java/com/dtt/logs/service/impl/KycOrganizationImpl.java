package com.dtt.logs.service.impl;

import com.dtt.logs.dto.KycOrganizationDto;
import com.dtt.logs.Model.KycOrganization;
import com.dtt.logs.repository.kyc.KycOrganizationRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KycOrganizationImpl {

    private final KycOrganizationRepository organizationRepository;

    public KycOrganizationImpl(KycOrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public String saveOrganization(KycOrganizationDto dto) {
        if (organizationRepository.existsByOrgId(dto.getOrgId())) {
            return "Organization already exists";
        }

        KycOrganization entity = new KycOrganization();
        entity.setOrgId(dto.getOrgId());
        entity.setOrgName(dto.getOrgName());
        entity.setSpocEmail(dto.getSpocEmail());
        entity.setOrgLogo(dto.getOrgLogo());
        entity.setSpocName(dto.getSpocName());
        entity.setSpocMobileNumber(dto.getSpocMobileNumber());
        entity.setInserted_at(LocalDateTime.now());

        organizationRepository.save(entity);
        return "Organization saved successfully";
    }
}
