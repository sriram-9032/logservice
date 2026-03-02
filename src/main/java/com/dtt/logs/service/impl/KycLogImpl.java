package com.dtt.logs.service.impl;

import com.dtt.logs.Model.KycServiceNameBasedCount;
import com.dtt.logs.Model.OrgidBasedCountSummary;
import com.dtt.logs.Model.SubTransactionsLog;
import com.dtt.logs.dto.*;
import com.dtt.logs.Model.AuditLog;
import com.dtt.logs.enums.central.LogMessageType;
import com.dtt.logs.enums.central.ServiceName;
import com.dtt.logs.enums.central.SignatureType;
import com.dtt.logs.enums.central.TransactionType;
import com.dtt.logs.repository.kyc.KycAuditRepository;
import com.dtt.logs.repository.kyc.ServiceNameBasedCountRepository;
import com.dtt.logs.repository.kyc.SubTransactionLogRepository;
import com.dtt.logs.service.iface.KycLogIface;

import com.mongodb.client.model.Filters;
import org.bson.Document;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class KycLogImpl implements KycLogIface {


    private final KycAuditRepository auditLogRepository;



    private final MongoTemplate mongoTemplate;


    private final ServiceNameBasedCountRepository serviceNameBasedCountRepository;


    private final SubTransactionLogRepository subTransactionLogRepository;

    public KycLogImpl(KycAuditRepository auditLogRepository, @Qualifier("kycLogMongoTemplate") MongoTemplate mongoTemplate, ServiceNameBasedCountRepository serviceNameBasedCountRepository, SubTransactionLogRepository subTransactionLogRepository) {
        this.auditLogRepository = auditLogRepository;
        this.mongoTemplate = mongoTemplate;
        this.serviceNameBasedCountRepository = serviceNameBasedCountRepository;
        this.subTransactionLogRepository = subTransactionLogRepository;
    }

    public Page<AuditLog> matchFilters(PageDto pageDTO, Pageable pageable) {
        LocalDateTime endDate = convertToLocalDateTime(pageDTO.getEndDate());
        LocalDateTime startDate = convertToLocalDateTime(pageDTO.getStartDate());

        Query query = new Query();

        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("timestamp").gte(startDate).lt(endDate));
        } else if (startDate != null) {
            query.addCriteria(Criteria.where("timestamp").gte(startDate));
        } else if (endDate != null) {
            query.addCriteria(Criteria.where("timestamp").lt(endDate));
        }

        if (pageDTO.getUserName() != null) {
            query.addCriteria(Criteria.where("userName").is(pageDTO.getUserName()));
        }

        if (pageDTO.getModuleName() != null) {
            query.addCriteria(Criteria.where("moduleName").is(pageDTO.getModuleName()));
        }

        query = query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize());

        List<AuditLog> auditLogs = mongoTemplate.find(query, AuditLog.class, "auditlogs");
        long total = mongoTemplate.count(query.skip(-1).limit(-1), AuditLog.class, "auditlogs");

        return new PageImpl<>(auditLogs, pageable, total);
    }

    public ResponseEntity<String> PostAudit(KycAuditDto dto) {
        try {
            // Convert Strings to Enums

            ServiceName serviceName = ServiceName.valueOf(dto.getServiceName());
            TransactionType transactionType = TransactionType.valueOf(dto.getTransactionType());
            LogMessageType logMessageType = LogMessageType.valueOf(dto.getLogMessageType().name());

            SignatureType signatureType = null;
            if (dto.getSignatureType() != null && !dto.getSignatureType().isEmpty()) {
                signatureType = SignatureType.valueOf(dto.getSignatureType());
            }

            // Parse timestamp
            LocalDateTime timestamp = (dto.getTimestamp() != null && !dto.getTimestamp().isEmpty())
                    ? LocalDateTime.parse(dto.getTimestamp())
                    : LocalDateTime.now();


            System.out.println("response:::"+serviceName);
            if (serviceName == ServiceName.OTHER)
            {
                System.out.println("hiiiii");
                SubTransactionsLog subTransactionsLog = new SubTransactionsLog(
                        dto.getIdentifier(),
                        dto.getCorrelationID(),
                        dto.getTransactionID(),
                        dto.getSubTransactionID(),
                        timestamp,
                        dto.getStartTime(),
                        dto.getEndTime(),
                        dto.getGeoLocation(),
                        dto.getCallStack(),
                        serviceName,
                        transactionType,
                        dto.getTransactionSubType(),
                        logMessageType,
                        dto.getLogMessage(),
                        dto.getServiceProviderName(),
                        dto.getServiceProviderAppName(),
                        signatureType,
                        dto.iseSealUsed(),
                        dto.getChecksum(),
                        dto.getDeviceId(),
                        dto.getSdkType(),
                        dto.getSdkVersion(),
                        dto.getTransactionPrice()
                );
                System.out.println("resppp::"+subTransactionsLog);
                subTransactionLogRepository.insert(subTransactionsLog);

            }

            else {
                AuditLog auditLog = new AuditLog(
                        dto.getIdentifier(),
                        dto.getCorrelationID(),
                        dto.getTransactionID(),
                        dto.getSubTransactionID(),
                        timestamp,
                        dto.getStartTime(),
                        dto.getEndTime(),
                        dto.getGeoLocation(),
                        dto.getCallStack(),
                        serviceName,
                        transactionType,
                        dto.getTransactionSubType(),
                        logMessageType,
                        dto.getLogMessage(),
                        dto.getServiceProviderName(),
                        dto.getServiceProviderAppName(),
                        signatureType,
                        dto.iseSealUsed(),
                        dto.getChecksum(),
                        dto.getDeviceId(),
                        dto.getSdkType(),
                        dto.getSdkVersion(),
                        dto.getTransactionPrice(),null
                );
                auditLogRepository.insert(auditLog);


            }

            this.upsertOrgidBasedCountSummary(dto);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public Page<AuditLog> getFilteredLogsAsPage(KycLogFilterDto dto, Pageable pageable) {
        Query query = new Query();

        if (dto.getOrganisationId() != null) {
            query.addCriteria(Criteria.where("serviceProviderName").is(dto.getOrganisationId()));
        }

        if (dto.getServiceNames() != null && !dto.getServiceNames().isEmpty()) {
            query.addCriteria(Criteria.where("serviceName").in(dto.getServiceNames()));
        }

        if (dto.getLogMessageTypes() != null && !dto.getLogMessageTypes().isEmpty()) {
            query.addCriteria(Criteria.where("logMessageType").in(dto.getLogMessageTypes()));
        }

        LocalDateTime timestamp = convertToLocalDateTime(dto.getTimestamp());
        if (timestamp != null) {
            query.addCriteria(Criteria.where("timestamp").is(timestamp));
        }

        query = query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize());

        List<AuditLog> logs = mongoTemplate.find(query, AuditLog.class, "auditlogs");
        long total = mongoTemplate.count(query.skip(-1).limit(-1), AuditLog.class, "auditlogs");

        return new PageImpl<>(logs, pageable, total);
    }


    public Page<AuditLog> getKycRecordsByDateRange(DateRangeFilterDto dto, Pageable pageable) {
        Query query = new Query();

        // Date Range Filter
        LocalDateTime from = convertToLocalDateTime(dto.getFromDate());
        LocalDateTime to = convertToLocalDateTime(dto.getToDate());

        if (from != null && to != null) {
            query.addCriteria(Criteria.where("timestamp").gte(from).lte(to));
        }

        // Optional Service Names
        if (dto.getServiceNames() != null && !dto.getServiceNames().isEmpty()) {
            query.addCriteria(Criteria.where("serviceName").in(dto.getServiceNames()));
        }

        query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize());

        List<AuditLog> logs = mongoTemplate.find(query, AuditLog.class, "auditlogs");

        // Count total without skip & limit
        long total = mongoTemplate.count(query.skip(-1).limit(-1), AuditLog.class, "auditlogs");

        return new PageImpl<>(logs, pageable, total);
    }

    public Page<AuditLog> getKycRecordsByOrgAndDate(String organizationId, DateRangeFilterDto dto, Pageable pageable) {
        Query query = new Query();

        query.addCriteria(Criteria.where("serviceProviderName").is(organizationId));

        LocalDateTime from = convertToLocalDateTime(dto.getFromDate());
        LocalDateTime to = convertToLocalDateTime(dto.getToDate());

        if (from != null && to != null) {
            query.addCriteria(Criteria.where("timestamp").gte(from).lte(to));
        }

        if (dto.getServiceNames() != null && !dto.getServiceNames().isEmpty()) {
            query.addCriteria(Criteria.where("serviceName").in(dto.getServiceNames()));
        }

        query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize());

        List<AuditLog> logs = mongoTemplate.find(query, AuditLog.class, "auditlogs");
        long total = mongoTemplate.count(query.skip(-1).limit(-1), AuditLog.class, "auditlogs");

        return new PageImpl<>(logs, pageable, total);
    }

    public AuditLog getRecordByTransactionId(String transactionId) {
        Query query = new Query(Criteria.where("transactionID").is(transactionId));
        return mongoTemplate.findOne(query, AuditLog.class, "auditlogs");
    }

    public List<AuditLog> getAllLogsSortedByLatest() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "timestamp"));
        return mongoTemplate.find(query, AuditLog.class, "auditlogs");
    }

    public List<OrgKycSummaryDto> getOrgKycSummary() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime startOfYear = now.withDayOfYear(1).with(LocalTime.MIN);
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        List<OrgKycSummaryDto> summaryList = new ArrayList<>();

        // Step 1: Get all orgs
        List<Document> orgs = mongoTemplate.findAll(Document.class, "organization-list");

        for (Document org : orgs) {
            String orgId = org.getString("orgId");
            String orgName = org.getString("orgName");
            String logo = org.getString("orgLogo");
            String spocEmail = org.getString("spocEmail");
            String spocName = org.getString("spocName");
            String spocMobile = org.getString("spocMobileNumber");

            // Step 2: Fetch KYC logs for this org
            Query query = new Query();
            query.addCriteria(Criteria.where("transactionType").is("KYC")
                    .and("serviceProviderName").is(orgId));

            List<Document> logs = mongoTemplate.find(query, Document.class, "auditlogs");

            long totalSuccess = 0;
            long totalFailed = 0;
            long monthSuccess = 0;
            long monthFailed = 0;
            long yearSuccess = 0;
            long yearFailed = 0;
            LocalDateTime lastSuccess = null;
            LocalDateTime lastFailed = null;

            // Initialize 12-month map
            Map<String, KycMonthlyStats> monthlyStats = new LinkedHashMap<>();
            for (int i = 11; i >= 0; i--) {
                YearMonth ym = YearMonth.now().minusMonths(i);
                String ymKey = ym.format(monthFormatter);
                monthlyStats.put(ymKey, new KycMonthlyStats());
            }

            for (Document log : logs) {
                String type = log.getString("logMessageType");
                Object tsRaw = log.get("timestamp");

                LocalDateTime ts = null;
                if (tsRaw instanceof Date) {
                    ts = ((Date) tsRaw).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                } else if (tsRaw instanceof String) {
                    try {
                        ts = LocalDateTime.parse((String) tsRaw, DateTimeFormatter.ISO_DATE_TIME);
                    } catch (Exception ignored) {
                        //
                    }
                }



                boolean isSuccess = "SUCCESS".equalsIgnoreCase(type);
                boolean isFailed = "FAILURE".equalsIgnoreCase(type);

                if (isSuccess) totalSuccess++;
                if (isFailed) totalFailed++;

                if (ts.isAfter(startOfMonth)) {
                    if (isSuccess) monthSuccess++;
                    if (isFailed) monthFailed++;
                }

                if (ts.isAfter(startOfYear)) {
                    if (isSuccess) yearSuccess++;
                    if (isFailed) yearFailed++;
                }

                if (isSuccess && (lastSuccess == null || ts.isAfter(lastSuccess))) {
                    lastSuccess = ts;
                }

                if (isFailed && (lastFailed == null || ts.isAfter(lastFailed))) {
                    lastFailed = ts;
                }

                String monthKey = YearMonth.from(ts).format(monthFormatter);
                if (monthlyStats.containsKey(monthKey)) {
                    KycMonthlyStats stat = monthlyStats.get(monthKey);
                    if (isSuccess) stat.setSuccessCount(stat.getSuccessCount() + 1);
                    if (isFailed) stat.setFailedCount(stat.getFailedCount() + 1);
                }
            }

            // Step 3: Build DTO
            OrgKycSummaryDto summary = new OrgKycSummaryDto();
            summary.setOrgId(orgId);
            summary.setOrgName(orgName);
            summary.setOrgLogo(logo);
            summary.setSpocEmail(spocEmail);
            summary.setSpocName(spocName);
            summary.setSpocMobileNumber(spocMobile);
            summary.setTotalKycCountSuccessful(totalSuccess);
            summary.setTotalKycCountFailed(totalFailed);
            summary.setTotalKycCountSuccessfulCurrentMonth(monthSuccess);
            summary.setTotalKycCountFailedCurrentMonth(monthFailed);
            summary.setTotalKycCountSuccessfulCurrentYear(yearSuccess);
            summary.setTotalKycCountFailedCurrentYear(yearFailed);
            summary.setLastKycSuccessfulTimestamp(lastSuccess);
            summary.setLastKycFailedTimestamp(lastFailed);
            summary.setMonthlyStats(monthlyStats);

            summaryList.add(summary);
        }

        return summaryList;
    }

    public OrgKycSummaryDto getOrgKycSummaryByOrgId(String orgId) {
        LocalDateTime now = LocalDateTime.now();

        // Step 1: Fetch Org details
        Query orgQuery = new Query(Criteria.where("orgId").is(orgId));
        Document org = mongoTemplate.findOne(orgQuery, Document.class, "organization-list");
        if (org == null) return null;

        String orgName = org.getString("orgName");
        String logo = org.getString("orgLogo");
        String spocEmail = org.getString("spocEmail");
        String spocName = org.getString("spocName");
        String spocMobile = org.getString("spocMobileNumber");

        // Step 2: Fetch KYC logs for this org
        Query logQuery = new Query(Criteria.where("transactionType").is("KYC")
                .and("serviceProviderName").is(orgId));
        List<Document> logs = mongoTemplate.find(logQuery, Document.class, "auditlogs");

        long totalSuccess = 0;
        long totalFailed = 0;
        long monthSuccess = 0;
        long monthFailed = 0;
        long yearSuccess = 0;
        long yearFailed = 0;
        LocalDateTime lastSuccess = null;
        LocalDateTime lastFailed = null;

        LocalDateTime startOfMonth = now.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime startOfYear = now.withDayOfYear(1).with(LocalTime.MIN);

        // Step 3: Prepare service-wise overall stats
        Map<String, KycStatsDto> serviceStats = new LinkedHashMap<>();

        // Step 4: Monthly stats for last 12 months
        Map<String, KycMonthlyStats> monthlyStats = new TreeMap<>(new MonthYearComparator());

        LocalDateTime monthPointer = now.minusMonths(11).withDayOfMonth(1).with(LocalTime.MIN);

        for (int i = 0; i < 12; i++) {
            String monthName = monthPointer.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                    + " " + monthPointer.getYear();

            monthlyStats.put(monthName, new KycMonthlyStats()); // initialize with 0,0
            monthPointer = monthPointer.plusMonths(1);
        }

        // Step 5: Process each log
        for (Document log : logs) {

            String serviceName = log.getString("serviceName");
            String type = log.getString("logMessageType");

            Object tsRaw = log.get("timestamp");
            LocalDateTime ts = null;

            if (tsRaw instanceof Date) {
                ts = ((Date) tsRaw).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else if (tsRaw instanceof String) {
                try {
                    ts = LocalDateTime.parse((String) tsRaw, DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception ignored) {
                    //
                }
            }


            boolean isSuccess = "SUCCESS".equalsIgnoreCase(type);
            boolean isFailed = "FAILURE".equalsIgnoreCase(type);

            // Overall totals
            if (isSuccess) totalSuccess++;
            if (isFailed) totalFailed++;

            // Month totals
            if (ts.isAfter(startOfMonth)) {
                if (isSuccess) monthSuccess++;
                if (isFailed) monthFailed++;
            }

            // Year totals
            if (ts.isAfter(startOfYear)) {
                if (isSuccess) yearSuccess++;
                if (isFailed) yearFailed++;
            }

            // Track latest timestamps
            if (isSuccess && (lastSuccess == null || ts.isAfter(lastSuccess))) {
                lastSuccess = ts;
            }
            if (isFailed && (lastFailed == null || ts.isAfter(lastFailed))) {
                lastFailed = ts;
            }

            // --- Update Monthly Stats ---
            String monthKey = ts.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                    + " " + ts.getYear();

            KycMonthlyStats m = monthlyStats.get(monthKey);
            if (m != null) {
                if (isSuccess) m.setSuccessCount(m.getSuccessCount() + 1);
                if (isFailed) m.setFailedCount(m.getFailedCount() + 1);
            }

            // --- Update service-wise stats ---
            serviceStats.computeIfAbsent(serviceName, k -> new KycStatsDto());
            KycStatsDto stats = serviceStats.get(serviceName);

            if (isSuccess) stats.setSuccessCount(stats.getSuccessCount() + 1);
            if (isFailed) stats.setFailedCount(stats.getFailedCount() + 1);
        }

        // Step 6: Build response DTO
        OrgKycSummaryDto summary = new OrgKycSummaryDto();

        summary.setOrgId(orgId);
        summary.setOrgName(orgName);
        summary.setOrgLogo(logo);
        summary.setSpocEmail(spocEmail);
        summary.setSpocName(spocName);
        summary.setSpocMobileNumber(spocMobile);

        summary.setTotalKycCountSuccessful(totalSuccess);
        summary.setTotalKycCountFailed(totalFailed);
        summary.setTotalKycCountSuccessfulCurrentMonth(monthSuccess);
        summary.setTotalKycCountFailedCurrentMonth(monthFailed);
        summary.setTotalKycCountSuccessfulCurrentYear(yearSuccess);
        summary.setTotalKycCountFailedCurrentYear(yearFailed);
        summary.setLastKycSuccessfulTimestamp(lastSuccess);
        summary.setLastKycFailedTimestamp(lastFailed);

        summary.setServiceStats(serviceStats);
        summary.setMonthlyStats(monthlyStats);

        return summary;
    }

    public KycOverallStatsDto getKycOverallStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).with(LocalTime.MIN);

        // Step 1: Org counts
        long totalOrgs = mongoTemplate.count(new Query(), "organization-list");
        Query newOrgQuery = new Query(Criteria.where("inserted_at").gte(startOfMonth));
        long newOrgsThisMonth = mongoTemplate.count(newOrgQuery, "organization-list");

        // Step 2: Overall KYC success/failure counts
        Query successQuery = new Query(Criteria.where("logMessageType").is("SUCCESS")
                .and("transactionType").is("KYC"));
        long totalSuccess = mongoTemplate.count(successQuery, "auditlogs");

        Query failedQuery = new Query(Criteria.where("logMessageType").is("FAILURE")
                .and("transactionType").is("KYC"));
        long totalFailed = mongoTemplate.count(failedQuery, "auditlogs");

        // Step 3: This month's KYC success/failure
        Query successThisMonth = new Query(Criteria.where("logMessageType").is("SUCCESS")
                .and("transactionType").is("KYC")
                .and("timestamp").gte(startOfMonth));
        long successThisMonthCount = mongoTemplate.count(successThisMonth, "auditlogs");

        Query failedThisMonth = new Query(Criteria.where("logMessageType").is("FAILURE")
                .and("transactionType").is("KYC")
                .and("timestamp").gte(startOfMonth));
        long failedThisMonthCount = mongoTemplate.count(failedThisMonth, "auditlogs");

        long totalKycsThisMonth = successThisMonthCount + failedThisMonthCount;

        // Step 4: Distinct services
        List<String> serviceNames = mongoTemplate.getCollection("auditlogs")
                .distinct("serviceName", Filters.eq("transactionType", "KYC"), String.class)
                .into(new ArrayList<>());

        // Step 5: Aggregate per-service stats
        Map<String, KycStatsDto> serviceStats = new LinkedHashMap<>();

        for (String serviceName : serviceNames) {
            Criteria serviceSuccess = Criteria.where("logMessageType").is("SUCCESS")
                    .and("transactionType").is("KYC")
                    .and("serviceName").is(serviceName);
            long successCount = mongoTemplate.count(new Query(serviceSuccess), "auditlogs");

            Criteria serviceFail = Criteria.where("logMessageType").is("FAILURE")
                    .and("transactionType").is("KYC")
                    .and("serviceName").is(serviceName);
            long failCount = mongoTemplate.count(new Query(serviceFail), "auditlogs");

            KycStatsDto stats = new KycStatsDto();
            stats.setSuccessCount(successCount);
            stats.setFailedCount(failCount);
            serviceStats.put(serviceName, stats);
        }

        // Step 6: Monthly stats for last 12 months (chronologically sorted)
        Map<String, Map<String, Long>> monthlyStats = new TreeMap<>(new MonthYearComparator());
        LocalDateTime monthPointer = now.minusMonths(11).withDayOfMonth(1).with(LocalTime.MIN);

        for (int i = 0; i < 12; i++) {
            LocalDateTime start = monthPointer.withDayOfMonth(1).with(LocalTime.MIN);
            LocalDateTime end = start.plusMonths(1).with(LocalTime.MIN);

            String monthName = start.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                    + " " + start.getYear();

            Criteria successCriteria = Criteria.where("logMessageType").is("SUCCESS")
                    .and("transactionType").is("KYC")
                    .and("timestamp").gte(start).lt(end);
            long successCount = mongoTemplate.count(new Query(successCriteria), "auditlogs");

            Criteria failCriteria = Criteria.where("logMessageType").is("FAILURE")
                    .and("transactionType").is("KYC")
                    .and("timestamp").gte(start).lt(end);
            long failCount = mongoTemplate.count(new Query(failCriteria), "auditlogs");

            Map<String, Long> counts = new HashMap<>();
            counts.put("successCount", successCount);
            counts.put("failedCount", failCount);

            monthlyStats.put(monthName, counts);
            monthPointer = monthPointer.plusMonths(1);
        }

        // Step 7: Build DTO
        KycOverallStatsDto dto = new KycOverallStatsDto();
        dto.setTotalServiceProviders(totalOrgs);
        dto.setNewServiceProvidersThisMonth(newOrgsThisMonth);
        dto.setTotalSuccessfulKycs(totalSuccess);
        dto.setTotalSuccessfulKycsThisMonth(successThisMonthCount);
        dto.setTotalFailedKycs(totalFailed);
        dto.setTotalKycsThisMonth(totalKycsThisMonth);
        dto.setServiceStats(serviceStats);
        dto.setMonthlyStats(monthlyStats);

        return dto;
    }


    public PageResultDto getLogsByIdentifier(IdentifierLogFilterDto dto) {
        Query query = new Query();



        if (dto.getIdentifier() != null && !dto.getIdentifier().trim().isEmpty()) {
            query.addCriteria(
                    Criteria.where("identifier")
                            .regex(".*" + Pattern.quote(dto.getIdentifier().trim()) + ".*", "i")
            );
        }

        if (dto.getLogMessageType() != null && !dto.getLogMessageType().isEmpty()) {
            query.addCriteria(Criteria.where("logMessageType").is(dto.getLogMessageType()));
        }

        if (dto.getOrgId() != null && !dto.getOrgId().isEmpty()) {
            query.addCriteria(Criteria.where("serviceProviderName").is(dto.getOrgId()));
        }

        // Filter by serviceNames
        if (dto.getServiceNames() != null && !dto.getServiceNames().isEmpty()) {
            query.addCriteria(Criteria.where("serviceName").in(dto.getServiceNames()));
        }

        // Filter by Dates
        if ((dto.getFromDate() != null && !dto.getFromDate().isEmpty()) ||
                (dto.getToDate() != null && !dto.getToDate().isEmpty())) {

            LocalDateTime from = convertToLocalDateTime(dto.getFromDate());
            LocalDateTime to = convertToLocalDateTime(dto.getToDate());

            if (from != null && to != null) {
                query.addCriteria(Criteria.where("timestamp").gte(from).lte(to));
            } else if (from != null) {
                query.addCriteria(Criteria.where("timestamp").gte(from));
            } else if (to != null) {
                query.addCriteria(Criteria.where("timestamp").lte(to));
            }
        }


        int page = Math.max(dto.getPage(), 1);
        int perPage = Math.max(dto.getPerPage(), 10);
        long total = mongoTemplate.count(query, "auditlogs");

        query.with(PageRequest.of(page - 1, perPage));
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));

        List<AuditLog> results = mongoTemplate.find(query, AuditLog.class, "auditlogs");

        PageResultDto response = new PageResultDto();
        response.setCurrentPage(page);
        response.setPerPage(perPage);
        response.setTotalCount(total);
        response.setTotalPages((int) Math.ceil((double) total / perPage));
        response.setData(results);

        return response;
    }

    public LocalDateTime convertToLocalDateTime(String time) {
        if (time != null && !time.isEmpty()) {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return null;
    }


    public ResponseEntity<String> ServiceNameBasedCountMethod(KycAuditDto dto) {
        try {
            String date = dto.getTimestamp().substring(0, 10);
            Optional<KycServiceNameBasedCount> existing = serviceNameBasedCountRepository.findByIdentifierAndServiceName(date, dto.getServiceName());

            if (existing.isPresent()) {

                System.out.println("Log Message from DTO: '" + dto.getLogMessageType() + "'");
                KycServiceNameBasedCount summary = existing.get();
                System.out.println("document  " + summary);
                if ("SUCCESS".equalsIgnoreCase(dto.getLogMessageType().name().trim())) {
                    System.out.println("inside sucess");
                    summary.setKycSucccount(summary.getKycSucccount() + 1);
                }
                if ("FAILURE".equalsIgnoreCase(dto.getLogMessageType().name().trim())) {
                    summary.setKycFailcount(summary.getKycFailcount() + 1);
                }
                summary.setUpdatedTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                serviceNameBasedCountRepository.save(summary);
                return new ResponseEntity<>("Record updated successfully", HttpStatus.OK);
            } else {

                KycServiceNameBasedCount newSummary = new KycServiceNameBasedCount();
                newSummary.setIdentifier(date);
                newSummary.setServiceName(dto.getServiceName());
                if (dto.getLogMessageType().equals(LogMessageType.SUCCESS.toString())) {
                    newSummary.setKycSucccount(1);
                    newSummary.setKycFailcount(0);
                } else if (dto.getLogMessageType().equals(LogMessageType.FAILURE.toString())) {
                    newSummary.setKycSucccount(0);
                    newSummary.setKycFailcount(1);
                } else {
                    newSummary.setKycSucccount(0);
                    newSummary.setKycFailcount(0);
                }
                newSummary.setOrgId(null);
                newSummary.setUpdatedTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                serviceNameBasedCountRepository.save(newSummary);
                return new ResponseEntity<>("New Record created successfully", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update or create a record", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> upsertOrgidBasedCountSummary(KycAuditDto dto) {
        try {
            if (dto.getTimestamp() != null && dto.getTimestamp().length() >= 10) {
                 //
            } else {
                throw new IllegalArgumentException("Timestamp is missing or too short");
            }
            String orgId = dto.getServiceProviderName();
            String serviceName = dto.getServiceName();
            String timestamp = dto.getTimestamp();
            String date = timestamp.substring(0, 10);
            String id = orgId + "-" + serviceName + "-" + date;

            String updatedTime = DateTimeFormatter.ISO_INSTANT.format(Instant.now());

            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update()
                    .set("orgId", orgId)
                    .set("serviceName", serviceName)
                    .set("identifier", date)
                    .set("updatedTime", updatedTime);

            if ("SUCCESS".equalsIgnoreCase(dto.getLogMessageType().name())) {
                update.inc("kycSuccCount", 1);
            } else {
                update.inc("kycFailCount", 1);
            }

            mongoTemplate.upsert(query, update, OrgidBasedCountSummary.class);

            return ResponseEntity.ok("Upsert successful for id: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Upsert failed: " + e.getMessage());
        }
    }


    @Override
    public ApiResponse verifyOrganizationTransaction(KycTransactionDto kycTransactionDto) {
        try{
            if(kycTransactionDto.getIdentifier()==null || kycTransactionDto.getIdentifier().isEmpty()){
                return new ApiResponse(true,"Identifier Cannot be null or empty",null);
            }
            if(kycTransactionDto.getServiceName().isEmpty()){
                return new ApiResponse(true,"Service Names List is empty",null);
            }
            if(kycTransactionDto.getDate()==null||kycTransactionDto.getDate().isEmpty()){
                return new ApiResponse(true,"Date cannot be null or empty",null);
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("serviceName").in(kycTransactionDto.getServiceName()));
            query.addCriteria(Criteria.where("identifier").in(kycTransactionDto.getIdentifier()));
            LocalDateTime timestamp = convertToLocalDateTime(kycTransactionDto.getDate());
            System.out.println("time stamp ---------------  "+ timestamp);
            LocalDateTime dateTime = convertToLocalDateTime(kycTransactionDto.getDate());
            LocalDateTime startOfDay = dateTime.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = dateTime.toLocalDate().atTime(LocalTime.MAX);

            query.addCriteria(Criteria.where("timestamp").gte(startOfDay).lte(endOfDay));
            query.addCriteria(Criteria.where("logMessageType").is("SUCCESS"));
            boolean isSuccess = mongoTemplate.exists(query, "auditlogs");
            if(isSuccess){
                return new ApiResponse(true,"Found",true);
            }
            else{
                return new ApiResponse(true,"Not Found",false);
            }
        }
        catch (Exception e){
            return new ApiResponse(false,"",null);
        }

    }

    @Override
    public ApiResponse getSubTransactionLog(String correlationId) {
        try{
            if(correlationId==null ||correlationId.isEmpty()){
                return new ApiResponse(true,"correlationId Cannot be null or empty",null);
            }

            Query query = new Query();
            query.addCriteria(Criteria.where("correlationID").is(correlationId));

            AuditLog subLog = mongoTemplate.findOne(query,AuditLog.class ,"auditlogs");
            List<AuditLog> ans = mongoTemplate.find(query,AuditLog.class ,"subTransactionsLog");
            ans.add(subLog);
            if(subLog==null){
                return new ApiResponse(false,"No Log Found With Given CorrelationId",null);
            }
            return new ApiResponse(true,"Log Found",ans);

        }
        catch (Exception e){
            return new ApiResponse(false,"",null);
        }

    }

    public ApiResponse getOverallKycCount() {
        try {
            GroupOperation groupTotals = Aggregation.group()
                    .sum("kycSuccCount").as("success")
                    .sum("kycFailCount").as("failure");

            Aggregation agg = Aggregation.newAggregation(groupTotals);
            List<Map> result = mongoTemplate.aggregate(agg, "orgid-based-count", Map.class).getMappedResults();

            Map<String, Integer> totals = new HashMap<>();
            if (!result.isEmpty()) {
                Map doc = result.get(0);
                totals.put("success", ((Number) doc.getOrDefault("success", 0)).intValue());
                totals.put("failure", ((Number) doc.getOrDefault("failure", 0)).intValue());
            } else {
                totals.put("success", 0);
                totals.put("failure", 0);
            }

            return new ApiResponse(true, "Overall KYC count fetched successfully", totals);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Integer> totals = new HashMap<>();
            totals.put("success", 0);
            totals.put("failure", 0);
            return new ApiResponse(false, "Error fetching KYC count: " + e.getMessage(), totals);
        }
    }


}
