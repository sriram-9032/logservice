package com.dtt.logs.service.iface;

import com.dtt.logs.dto.*;
import com.dtt.logs.Model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface KycLogIface {

    Page<AuditLog> getKycRecordsByDateRange(DateRangeFilterDto dto, Pageable pageable);

    Page<AuditLog> getKycRecordsByOrgAndDate(String organizationId, DateRangeFilterDto dto, Pageable pageable);

    AuditLog getRecordByTransactionId(String transactionId);

    List<AuditLog> getAllLogsSortedByLatest();

    public Page<AuditLog> matchFilters(PageDto pageDTO, Pageable pageable);

    public ResponseEntity<String> PostAudit(KycAuditDto auditDTO);

    public Page<AuditLog> getFilteredLogsAsPage(KycLogFilterDto dto, Pageable pageable);

    ApiResponse verifyOrganizationTransaction(KycTransactionDto kycTransactionDto);

    ApiResponse getSubTransactionLog(String correlationId);
}
