package com.example.beliemeserver.data.entity.old;

import com.example.beliemeserver.data.entity.DataEntity;
import com.example.beliemeserver.data.entity.id.*;
import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.old.OldHistoryDto;
import com.example.beliemeserver.model.dto.old.OldUserDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "old_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Accessors(chain = true)
@IdClass(OldHistoryId.class)
public class OldHistoryEntity implements DataEntity {
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
    private OldItemEntity item;

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
    private OldUserEntity requester;

    @ManyToOne
    @JoinColumn(name = "approve_manager_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private OldUserEntity approveManager;

    @ManyToOne
    @JoinColumn(name = "return_manager_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private OldUserEntity returnManager;

    @ManyToOne
    @JoinColumn(name = "lost_manager_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private OldUserEntity lostManager;

    @ManyToOne
    @JoinColumn(name = "cancel_manager_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private OldUserEntity cancelManager;

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

    public OldHistoryDto toHistoryDto() throws FormatDoesNotMatchException {
        OldUserDto requesterDto = null;
        OldUserDto approveManagerDto = null;
        OldUserDto returnManagerDto = null;
        OldUserDto lostManagerDto = null;
        OldUserDto cancelManagerDto = null;

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

        return OldHistoryDto.builder()
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

    public OldHistoryDto toHistoryDtoNestedToItem() throws FormatDoesNotMatchException {
        OldUserDto requesterDto = null;
        OldUserDto approveManagerDto = null;
        OldUserDto returnManagerDto = null;
        OldUserDto lostManagerDto = null;
        OldUserDto cancelManagerDto = null;

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
        return OldHistoryDto.builder()
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