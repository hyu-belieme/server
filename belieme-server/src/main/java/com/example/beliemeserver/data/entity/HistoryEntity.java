package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "history", uniqueConstraints = {
        @UniqueConstraint(
                name = "history_index",
                columnNames = {"item_id", "num"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class HistoryEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "item_id")
    private int itemId;

    @Setter
    @Column(name = "num")
    private int num;

    @Column(name = "requester_id")
    private Integer requesterId;

    @Column(name = "approve_manager_id")
    private Integer approveManagerId;

    @Column(name = "return_manager_id")
    private Integer returnManagerId;

    @Column(name = "lost_manager_id")
    private Integer lostManagerId;

    @Column(name = "cancel_manager_id")
    private Integer cancelManagerId;

    @Setter
    @Column(name = "reserved_time_stamp")
    private long reservedTimeStamp;

    @Setter
    @Column(name = "approve_time_stamp")
    private long approveTimeStamp;

    @Setter
    @Column(name = "return_time_stamp")
    private long returnTimeStamp;

    @Setter
    @Column(name = "lost_time_stamp")
    private long lostTimeStamp;

    @Setter
    @Column(name = "cancel_time_stamp")
    private long cancelTimeStamp;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity requester;

    @ManyToOne
    @JoinColumn(name = "approve_manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity approveManager;

    @ManyToOne
    @JoinColumn(name = "return_manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity returnManager;

    @ManyToOne
    @JoinColumn(name = "lost_manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity lostManager;

    @ManyToOne
    @JoinColumn(name = "cancel_manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity cancelManager;

    public HistoryEntity(
            @NonNull ItemEntity item, int num, UserEntity requester, UserEntity approveManager,
            UserEntity returnManager, UserEntity lostManager, UserEntity cancelManager,
            long reservedTimeStamp, long approveTimeStamp, long returnTimeStamp,
            long lostTimeStamp, long cancelTimeStamp
    ) {
        setItem(item);
        setNum(num);

        setRequester(requester);
        setApproveManager(approveManager);
        setReturnManager(returnManager);
        setLostManager(lostManager);
        setCancelManager(cancelManager);

        setReservedTimeStamp(reservedTimeStamp);
        setApproveTimeStamp(approveTimeStamp);
        setReturnTimeStamp(returnTimeStamp);
        setLostTimeStamp(lostTimeStamp);
        setCancelTimeStamp(cancelTimeStamp);
    }

    public void setItem(@NonNull ItemEntity item) {
        this.item = item;
        this.itemId = item.getId();
    }

    public void setRequester(UserEntity requester) {
        this.requester = requester;
        this.requesterId = requester.getId();
    }

    public void setApproveManager(UserEntity approveManager) {
        this.approveManager = approveManager;
        this.approveManagerId = getIdOrElse(approveManager, null);
    }

    public void setReturnManager(UserEntity returnManager) {
        this.returnManager = returnManager;
        this.returnManagerId = getIdOrElse(returnManager, null);
    }

    public void setLostManager(UserEntity lostManager) {
        this.lostManager = lostManager;
        this.lostManagerId = getIdOrElse(lostManager, null);
    }

    public void setCancelManager(UserEntity cancelManager) {
        this.cancelManager = cancelManager;
        this.cancelManagerId = getIdOrElse(cancelManager, null);
    }

    public HistoryDto toHistoryDto() throws FormatDoesNotMatchException {
        return new HistoryDto(
                item.toItemDto(),
                num,
                getUserDtoOrNull(requester),
                getUserDtoOrNull(approveManager),
                getUserDtoOrNull(returnManager),
                getUserDtoOrNull(lostManager),
                getUserDtoOrNull(cancelManager),
                reservedTimeStamp,
                approveTimeStamp,
                returnTimeStamp,
                lostTimeStamp,
                cancelTimeStamp
        );
    }

    public HistoryDto toHistoryDtoNestedToItem() throws FormatDoesNotMatchException {
        return new HistoryDto(
                ItemDto.nestedEndpoint,
                num,
                getUserDtoOrNull(requester),
                getUserDtoOrNull(approveManager),
                getUserDtoOrNull(returnManager),
                getUserDtoOrNull(lostManager),
                getUserDtoOrNull(cancelManager),
                reservedTimeStamp,
                approveTimeStamp,
                returnTimeStamp,
                lostTimeStamp,
                cancelTimeStamp
        );
    }

    private static UserDto getUserDtoOrNull(UserEntity user) throws FormatDoesNotMatchException {
        if(user == null) {
            return null;
        }
        return user.toUserDto();
    }
}
