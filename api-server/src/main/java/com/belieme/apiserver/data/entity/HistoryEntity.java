package com.belieme.apiserver.data.entity;

import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "HISTORY_", uniqueConstraints = {
        @UniqueConstraint(
                name = "history_index",
                columnNames = {"item_id", "num"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class HistoryEntity extends DataEntity<UUID> {
    @Id
    @NonNull
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "item_id", columnDefinition = "BINARY(16)")
    private UUID itemId;

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

    @Column(name = "requested_at")
    private long requestedAt;

    @Column(name = "approved_at")
    private long approvedAt;

    @Column(name = "returned_at")
    private long returnedAt;

    @Column(name = "lost_at")
    private long lostAt;

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
            @NonNull UUID id, @NonNull ItemEntity item, int num, UserEntity requester, UserEntity approveManager,
            UserEntity returnManager, UserEntity lostManager, UserEntity cancelManager,
            long requestedAt, long approvedAt, long returnedAt,
            long lostAt, long canceledAt
    ) {
        this.id = id;

        this.item = item;
        this.itemId = item.getId();

        this.num = num;

        this.requester = requester;
        this.requesterId = getIdOrElse(requester, null);

        this.approveManager = approveManager;
        this.approveManagerId = getIdOrElse(approveManager, null);

        this.returnManager = returnManager;
        this.returnManagerId = getIdOrElse(returnManager, null);

        this.lostManager = lostManager;
        this.lostManagerId = getIdOrElse(lostManager, null);

        this.cancelManager = cancelManager;
        this.cancelManagerId = getIdOrElse(cancelManager, null);

        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.returnedAt = returnedAt;
        this.lostAt = lostAt;
        this.canceledAt = canceledAt;
    }

    public HistoryEntity withItem(@NonNull ItemEntity item) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withNum(int num) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withRequester(UserEntity requester) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withApproveManager(UserEntity approveManager) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withReturnManager(UserEntity returnManager) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withLostManager(UserEntity lostManager) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withCancelManager(UserEntity cancelManager) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withRequestedAt(long requestedAt) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withApprovedAt(long approvedAt) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }
    public HistoryEntity withReturnedAt(long returnedAt) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withLostAt(long lostAt) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public HistoryEntity withCanceledAt(long canceledAt) {
        return new HistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
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

    private static UserDto getUserDtoOrNull(UserEntity user) {
        if (user == null) {
            return null;
        }
        return user.toUserDto();
    }
}
