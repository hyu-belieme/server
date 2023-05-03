package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "new_history", uniqueConstraints = {
        @UniqueConstraint(
                name = "new_history_index",
                columnNames = {"item_id", "num"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class NewHistoryEntity extends NewDataEntity<UUID> {
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

    public NewHistoryEntity withItem(@NonNull NewItemEntity item) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withNum(int num) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withRequester(@NonNull NewUserEntity requester) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withApproveManager(@NonNull NewUserEntity approveManager) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withReturnManager(@NonNull NewUserEntity returnManager) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withLostManager(@NonNull NewUserEntity lostManager) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withCancelManager(@NonNull NewUserEntity cancelManager) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withRequestedAt(long requestedAt) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withApprovedAt(long approvedAt) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }
    public NewHistoryEntity withReturnedAt(long returnedAt) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withLostAt(long lostAt) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
    }

    public NewHistoryEntity withCanceledAt(long canceledAt) {
        return new NewHistoryEntity(id, item, num, requester, approveManager, returnManager, lostManager, cancelManager, requestedAt, approvedAt, returnedAt, lostAt, canceledAt);
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
