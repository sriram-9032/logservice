package com.dtt.logs.repository.kyc;

import com.dtt.logs.Model.KycOrganization;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KycOrganizationRepository extends MongoRepository<KycOrganization, String> {
    boolean existsByOrgId(String orgId);
}
