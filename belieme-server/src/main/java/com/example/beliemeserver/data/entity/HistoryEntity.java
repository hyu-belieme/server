package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.domain.dto.HistoryDto;
import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

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
@Accessors(chain = true)
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
    @Column(name = "requested_at")
    private long requestedAt;

    @Setter
    @Column(name = "approved_at")
    private long approvedAt;

    @Setter
    @Column(name = "returned_at")
    private long returnedAt;

    @Setter
    @Column(name = "lost_at")
    private long lostAt;

    @Setter
    @Column(name = "canceled_at")
    private long canceledAt;

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
            long requestedAt, long approvedAt, long returnedAt,
            long lostAt, long canceledAt
    ) {
        setItem(item);
        setNum(num);

        setRequester(requester);
        setApproveManager(approveManager);
        setReturnManager(returnManager);
        setLostManager(lostManager);
        setCancelManager(cancelManager);

        setRequestedAt(requestedAt);
        setApprovedAt(approvedAt);
        setReturnedAt(returnedAt);
        setLostAt(lostAt);
        setCanceledAt(canceledAt);
    }

    public HistoryEntity setItem(@NonNull ItemEntity item) {
        this.item = item;
        this.itemId = item.getId();
        return this;
    }

    public HistoryEntity setRequester(UserEntity requester) {
        this.requester = requester;
        this.requesterId = getIdOrElse(requester, null);
        return this;
    }

    public HistoryEntity setApproveManager(UserEntity approveManager) {
        this.approveManager = approveManager;
        this.approveManagerId = getIdOrElse(approveManager, null);
        return this;
    }

    public HistoryEntity setReturnManager(UserEntity returnManager) {
        this.returnManager = returnManager;
        this.returnManagerId = getIdOrElse(returnManager, null);
        return this;
    }

    public HistoryEntity setLostManager(UserEntity lostManager) {
        this.lostManager = lostManager;
        this.lostManagerId = getIdOrElse(lostManager, null);
        return this;
    }

    public HistoryEntity setCancelManager(UserEntity cancelManager) {
        this.cancelManager = cancelManager;
        this.cancelManagerId = getIdOrElse(cancelManager, null);
        return this;
    }

    public HistoryDto toHistoryDto() {
        return new HistoryDto(
                item.toItemDto(),
                num,
                getUserDtoOrNull(requester),
                getUserDtoOrNull(approveManager),
                getUserDtoOrNull(returnManager),
                getUserDtoOrNull(lostManager),
                getUserDtoOrNull(cancelManager),
                requestedAt,
                approvedAt,
                returnedAt,
                lostAt,
                canceledAt
        );
    }

    public HistoryDto toHistoryDtoNestedToItem() {
        return new HistoryDto(
                ItemDto.nestedEndpoint,
                num,
                getUserDtoOrNull(requester),
                getUserDtoOrNull(approveManager),
                getUserDtoOrNull(returnManager),
                getUserDtoOrNull(lostManager),
                getUserDtoOrNull(cancelManager),
                requestedAt,
                approvedAt,
                returnedAt,
                lostAt,
                canceledAt
        );
    }

    private static UserDto getUserDtoOrNull(UserEntity user) {
        if (user == null) {
            return null;
        }
        return user.toUserDto();
    }
}
