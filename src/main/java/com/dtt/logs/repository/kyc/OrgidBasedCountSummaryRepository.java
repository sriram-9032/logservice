package com.dtt.logs.repository.kyc;

import com.dtt.logs.Model.OrgidBasedCountSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrgidBasedCountSummaryRepository extends MongoRepository<OrgidBasedCountSummary, String> {
}
