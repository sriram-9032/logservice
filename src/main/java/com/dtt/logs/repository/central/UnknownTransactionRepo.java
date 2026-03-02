package com.dtt.logs.repository.central;

import com.dtt.logs.Model.UnknownTransactions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnknownTransactionRepo extends MongoRepository<UnknownTransactions,String>{
}
