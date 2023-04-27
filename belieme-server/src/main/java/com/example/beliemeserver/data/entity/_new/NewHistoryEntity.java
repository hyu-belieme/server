package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "new_history", uniqueConstraints = {
        @UniqueConstraint(
                name = "history_index",
                columnNames = {"item_id", "num"}
        )
})
@NoArgsConstructor
@ToString
@Getter
@Accessors(chain = true)
public class NewHistoryEntity extends NewDataEntity<UUID> {
    @Id
    @NonNull
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "item_id", columnDefinition = "BINARY(16)")
    private UUID itemId;

    @Setter
    @Column(name = "num")
    private int num;

    @Column(name = "requester_id", columnDefinition = "BINARY(16)")
    private UUID requesterId;

    @Column(name = "approve_manager_id", columnDefinition = "BINARY(16)")
    private UUID approveManagerId;

    @Column(name = "return_manager_id", columnDefinition = "BINARY(16)")
    private UUID returnManagerId;

    @Column(name = "lost_manager_id", columnDefinition = "BINARY(16)")
    private UUID lostManagerId;

    @Column(name = "cancel_manager_id", columnDefinition = "BINARY(16)")
    private UUID cancelManagerId;

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
    private NewItemEntity item;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUserEntity requester;

    @ManyToOne
    @JoinColumn(name = "approve_manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUserEntity approveManager;

    @ManyToOne
    @JoinColumn(name = "return_manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUserEntity returnManager;

    @ManyToOne
    @JoinColumn(name = "lost_manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUserEntity lostManager;

    @ManyToOne
    @JoinColumn(name = "cancel_manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUserEntity cancelManager;

    public NewHistoryEntity(
            @NonNull UUID id, @NonNull NewItemEntity item, int num, NewUserEntity requester, NewUserEntity approveManager,
            NewUserEntity returnManager, NewUserEntity lostManager, NewUserEntity cancelManager,
            long requestedAt, long approvedAt, long returnedAt,
            long lostAt, long canceledAt
    ) {
        this.id = id;
        this.item = item;
        this.itemId = item.getId();
        this.num = num;

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

    public NewHistoryEntity setItem(@NonNull NewItemEntity item) {
        this.item = item;
        this.itemId = item.getId();
        return this;
    }

    public NewHistoryEntity setRequester(NewUserEntity requester) {
        this.requester = requester;
        this.requesterId = getIdOrElse(requester, null);
        return this;
    }

    public NewHistoryEntity setApproveManager(NewUserEntity approveManager) {
        this.approveManager = approveManager;
        this.approveManagerId = getIdOrElse(approveManager, null);
        return this;
    }

    public NewHistoryEntity setReturnManager(NewUserEntity returnManager) {
        this.returnManager = returnManager;
        this.returnManagerId = getIdOrElse(returnManager, null);
        return this;
    }

    public NewHistoryEntity setLostManager(NewUserEntity lostManager) {
        this.lostManager = lostManager;
        this.lostManagerId = getIdOrElse(lostManager, null);
        return this;
    }

    public NewHistoryEntity setCancelManager(NewUserEntity cancelManager) {
        this.cancelManager = cancelManager;
        this.cancelManagerId = getIdOrElse(cancelManager, null);
        return this;
    }

    public HistoryDto toHistoryDto() {
        return new HistoryDto(
                id,
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
                id,
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

    private static UserDto getUserDtoOrNull(NewUserEntity user) {
        if (user == null) {
            return null;
        }
        return user.toUserDto();
    }
}
