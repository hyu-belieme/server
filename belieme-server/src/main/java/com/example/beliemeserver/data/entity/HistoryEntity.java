package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.*;
import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Accessors(chain = true)
@IdClass(HistoryId.class)
public class HistoryEntity {
    @Id
    @Column(name = "stuff_id")
    private int stuffId;

    @Id
    @Column(name = "item_num")
    private int itemNum;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "stuff_id", referencedColumnName = "stuff_id", insertable = false, updatable = false),
            @JoinColumn(name = "item_num", referencedColumnName = "num", insertable = false, updatable = false)
    })
    private ItemEntity item;

    @Id
    @Column(name = "num")
    private int num;

    @Column(name = "requester_id")
    private String requesterId;

    @Column(name = "approve_manager_id")
    private String approveManagerId;

    @Column(name = "return_manager_id")
    private String returnManagerId;

    @Column(name = "lost_manager_id")
    private String lostManagerId;

    @Column(name = "cancel_manager_id")
    private String cancelManagerId;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private UserEntity requester;

    @ManyToOne
    @JoinColumn(name = "approve_manager_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private UserEntity approveManager;

    @ManyToOne
    @JoinColumn(name = "return_manager_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private UserEntity returnManager;

    @ManyToOne
    @JoinColumn(name = "lost_manager_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private UserEntity lostManager;

    @ManyToOne
    @JoinColumn(name = "cancel_manager_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private UserEntity cancelManager;

    @Column(name = "reserved_time_stamp")
    private long reservedTimeStamp;

    @Column(name = "approve_time_stamp")
    private long approveTimeStamp;

    @Column(name = "return_time_stamp")
    private long returnTimeStamp;

    @Column(name = "lost_time_stamp")
    private long lostTimeStamp;

    @Column(name = "cancel_time_stamp")
    private long cancelTimeStamp;

    public HistoryDto toHistoryDto() throws FormatDoesNotMatchException {
        UserDto requesterDto = null;
        UserDto approveManagerDto = null;
        UserDto returnManagerDto = null;
        UserDto lostManagerDto = null;
        UserDto cancelManagerDto = null;

        if(requester != null) {
            requesterDto = requester.toUserDto();
        }
        if(approveManager != null) {
            approveManagerDto = approveManager.toUserDto();
        }
        if(returnManager != null) {
            returnManagerDto = returnManager.toUserDto();
        }
        if(lostManager != null) {
            lostManagerDto = lostManager.toUserDto();
        }
        if(cancelManager != null) {
            cancelManagerDto = cancelManager.toUserDto();
        }

        return HistoryDto.builder()
                .item(item.toItemDto())
                .num(num)
                .requester(requesterDto)
                .approveManager(approveManagerDto)
                .returnManager(returnManagerDto)
                .lostManager(lostManagerDto)
                .cancelManager(cancelManagerDto)
                .reservedTimeStamp(reservedTimeStamp)
                .approveTimeStamp(approveTimeStamp)
                .returnTimeStamp(returnTimeStamp)
                .lostTimeStamp(lostTimeStamp)
                .cancelTimeStamp(cancelTimeStamp)
                .build();
    }

    public HistoryDto toHistoryDtoNestedToItem() throws FormatDoesNotMatchException {
        UserDto requesterDto = null;
        UserDto approveManagerDto = null;
        UserDto returnManagerDto = null;
        UserDto lostManagerDto = null;
        UserDto cancelManagerDto = null;

        if(requester != null) {
            requesterDto = requester.toUserDto();
        }
        if(approveManager != null) {
            approveManagerDto = approveManager.toUserDto();
        }
        if(returnManager != null) {
            returnManagerDto = returnManager.toUserDto();
        }
        if(lostManager != null) {
            lostManagerDto = lostManager.toUserDto();
        }
        if(cancelManager != null) {
            cancelManagerDto = cancelManager.toUserDto();
        }
        return HistoryDto.builder()
                .item(null)
                .num(num)
                .requester(requesterDto)
                .approveManager(approveManagerDto)
                .returnManager(returnManagerDto)
                .lostManager(lostManagerDto)
                .cancelManager(cancelManagerDto)
                .reservedTimeStamp(reservedTimeStamp)
                .approveTimeStamp(approveTimeStamp)
                .returnTimeStamp(returnTimeStamp)
                .lostTimeStamp(lostTimeStamp)
                .cancelTimeStamp(cancelTimeStamp)
                .build();
    }
}