package com.dtt.logs.repository.kyc;

import com.dtt.logs.Model.SubTransactionsLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTransactionLogRepository  extends MongoRepository<SubTransactionsLog,String> {
}
