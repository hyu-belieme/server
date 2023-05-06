package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.UUID;

@Getter
@JsonPropertyOrder({"id", "univ", "dept", "item", "num", "status", "requestedAt", "requester", "approvedAt", "approveManager", "lostAt", "lostManager", "returnedAt", "returnManager", "canceledAt", "cancelManager"})
public class HistoryResponse extends JsonResponse {
    private UUID id;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private UniversityResponse university;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private DepartmentResponse department;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private ItemResponse item;
    private int num;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse requester;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse approveManager;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse returnManager;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse lostManager;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse cancelManager;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long requestedAt;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long approvedAt;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long returnedAt;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long lostAt;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long canceledAt;

    private String status;

    private HistoryResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private HistoryResponse(UUID id, UniversityResponse university, DepartmentResponse department, ItemResponse item, int num, UserResponse requester, UserResponse approveManager, UserResponse returnManager, UserResponse lostManager, UserResponse cancelManager, long requestedAt, long approvedAt, long returnedAt, long lostAt, long canceledAt, String status) {
        super(true);
        this.id = id;
        this.university = university;
        this.department = department;
        this.item = item;
        this.num = num;
        this.requester = requester;
        this.approveManager = approveManager;
        this.returnManager = returnManager;
        this.lostManager = lostManager;
        this.cancelManager = cancelManager;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.returnedAt = returnedAt;
        this.lostAt = lostAt;
        this.canceledAt = canceledAt;
        this.status = status;
    }

    public static HistoryResponse responseWillBeIgnore() {
        return new HistoryResponse(false);
    }

    public static HistoryResponse from(HistoryDto historyDto) {
        if (historyDto == null) return null;
        if (historyDto.equals(HistoryDto.nestedEndpoint)) {
            return HistoryResponse.responseWillBeIgnore();
        }

        return new HistoryResponse(
                historyDto.id(),
                UniversityResponse.from(historyDto.item().stuff().department().university()),
                DepartmentResponse.from(historyDto.item().stuff().department()).withoutUniversity(),
                ItemResponse.from(historyDto.item()).withoutUniversityAndDepartment(),
                historyDto.num(),
                toNestedResponse(UserResponse.from(historyDto.requester())),
                toNestedResponse(UserResponse.from(historyDto.approveManager())),
                toNestedResponse(UserResponse.from(historyDto.returnManager())),
                toNestedResponse(UserResponse.from(historyDto.lostManager())),
                toNestedResponse(UserResponse.from(historyDto.cancelManager())),
                historyDto.requestedAt(), historyDto.approvedAt(),
                historyDto.returnedAt(), historyDto.lostAt(),
                historyDto.canceledAt(), historyDto.status().toString());
    }

    public HistoryResponse withoutUniversityAndDepartment() {
        return new HistoryResponse(
                id,
                UniversityResponse.responseWillBeIgnore(), DepartmentResponse.responseWillBeIgnore(),
                item, num, requester, approveManager,
                returnManager, lostManager, cancelManager, requestedAt,
                approvedAt, returnedAt, lostAt, canceledAt, status);
    }

    public HistoryResponse withoutItem() {
        return new HistoryResponse(
                id, university, department,
                ItemResponse.responseWillBeIgnore(), num, requester, approveManager,
                returnManager, lostManager, cancelManager, requestedAt,
                approvedAt, returnedAt, lostAt, canceledAt, status);
    }

    private static UserResponse toNestedResponse(UserResponse user) {
        if (user == null) return null;
        return user.withoutSecureInfo();
    }
}
