package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.HistoryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"univ", "dept", "item", "num", "status", "reservedTimeStamp", "requester", "approveTimeStamp", "approveManager", "lostTimeStamp", "lostManager", "returnTimeStamp", "returnManager", "cancelTimeStamp", "cancelManager"})
public class HistoryResponse extends JsonResponse {
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
    private long reservedTimeStamp;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long approveTimeStamp;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long returnTimeStamp;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long lostTimeStamp;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long cancelTimeStamp;

    private String status;

    private HistoryResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private HistoryResponse(UniversityResponse university, DepartmentResponse department, ItemResponse item, int num, UserResponse requester, UserResponse approveManager, UserResponse returnManager, UserResponse lostManager, UserResponse cancelManager, long reservedTimeStamp, long approveTimeStamp, long returnTimeStamp, long lostTimeStamp, long cancelTimeStamp, String status) {
        super(true);
        this.university = university;
        this.department = department;
        this.item = item;
        this.num = num;
        this.requester = requester;
        this.approveManager = approveManager;
        this.returnManager = returnManager;
        this.lostManager = lostManager;
        this.cancelManager = cancelManager;
        this.reservedTimeStamp = reservedTimeStamp;
        this.approveTimeStamp = approveTimeStamp;
        this.returnTimeStamp = returnTimeStamp;
        this.lostTimeStamp = lostTimeStamp;
        this.cancelTimeStamp = cancelTimeStamp;
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
                UniversityResponse.from(historyDto.item().stuff().department().university()),
                DepartmentResponse.from(historyDto.item().stuff().department()).withoutUniversity(),
                ItemResponse.from(historyDto.item()).withoutUniversityAndDepartment(),
                historyDto.num(),
                toNestedResponse(UserResponse.from(historyDto.requester())),
                toNestedResponse(UserResponse.from(historyDto.approveManager())),
                toNestedResponse(UserResponse.from(historyDto.returnManager())),
                toNestedResponse(UserResponse.from(historyDto.lostManager())),
                toNestedResponse(UserResponse.from(historyDto.cancelManager())),
                historyDto.reservedTimeStamp(), historyDto.approveTimeStamp(),
                historyDto.returnTimeStamp(), historyDto.lostTimeStamp(),
                historyDto.cancelTimeStamp(), historyDto.status().toString());
    }

    public HistoryResponse withoutUniversityAndDepartment() {
        return new HistoryResponse(
                UniversityResponse.responseWillBeIgnore(), DepartmentResponse.responseWillBeIgnore(),
                item, num, requester, approveManager,
                returnManager, lostManager, cancelManager, reservedTimeStamp,
                approveTimeStamp, returnTimeStamp, lostTimeStamp, cancelTimeStamp, status);
    }

    public HistoryResponse withoutItem() {
        return new HistoryResponse(
                university, department,
                ItemResponse.responseWillBeIgnore(), num, requester, approveManager,
                returnManager, lostManager, cancelManager, reservedTimeStamp,
                approveTimeStamp, returnTimeStamp, lostTimeStamp, cancelTimeStamp, status);
    }

    private static UserResponse toNestedResponse(UserResponse user) {
        if (user == null) return null;
        return user.withoutSecureInfo();
    }
}
