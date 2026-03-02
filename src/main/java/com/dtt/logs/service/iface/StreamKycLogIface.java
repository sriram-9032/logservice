package com.dtt.logs.service.iface;

import com.dtt.logs.Model.AuditLog;
import reactor.core.publisher.Flux;


public interface StreamKycLogIface {

    Flux<AuditLog> getAllStreamedLogsSortedByLatest();

}
