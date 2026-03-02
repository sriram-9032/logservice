package com.dtt.logs.repository.kyc;

import com.dtt.logs.Model.KycServiceNameBasedCount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceNameBasedCountRepository extends MongoRepository<KycServiceNameBasedCount,String> {
    @Query("{ 'identifier': ?0, 'serviceName': ?1 }")
    Optional<KycServiceNameBasedCount> findByIdentifierAndServiceName(String identifier, String serviceName);

}
