package com.dtt.logs.service.iface;

import com.dtt.logs.dto.AuditDto;
import com.dtt.logs.dto.PageDto;
import com.dtt.logs.Model.AdminAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminLogIface {

    public Page<AdminAuditLog> matchFilters(PageDto pageDTO, Pageable pageable);

    public ResponseEntity<String> PostAudit(AuditDto auditDTO);
}
