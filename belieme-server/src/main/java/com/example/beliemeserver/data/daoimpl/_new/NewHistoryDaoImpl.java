package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewHistoryEntity;
import com.example.beliemeserver.data.entity._new.NewItemEntity;
import com.example.beliemeserver.data.entity._new.NewStuffEntity;
import com.example.beliemeserver.data.entity._new.NewUserEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.HistoryDao;
import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewHistoryDaoImpl extends NewBaseDaoImpl implements HistoryDao {
    public NewHistoryDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<HistoryDto> getAllList() {
        return toHistoryDtoList(historyRepository.findAll());
    }

    @Override
    public List<HistoryDto> getListByDepartment(@NonNull UUID departmentId) {
        List<HistoryDto> output = new ArrayList<>();
        for (NewStuffEntity stuff : stuffRepository.findByDepartmentId(departmentId)) {
            for (NewItemEntity item : itemRepository.findByStuffId(stuff.getId())) {
                output.addAll(toHistoryDtoList(historyRepository.findByItemId(item.getId())));
            }
        }
        return output;
    }

    @Override
    public List<HistoryDto> getListByStuff(@NonNull UUID stuffId) {
        List<HistoryDto> output = new ArrayList<>();
        for (NewItemEntity item : itemRepository.findByStuffId(stuffId)) {
            output.addAll(toHistoryDtoList(historyRepository.findByItemId(item.getId())));
        }
        return output;
    }

    @Override
    public List<HistoryDto> getListByItem(@NonNull UUID itemId) {
        return toHistoryDtoList(historyRepository.findByItemId(itemId));
    }

    @Override
    public List<HistoryDto> getListByDepartmentAndRequester(@NonNull UUID departmentId, @NonNull UUID requesterId) {
        List<HistoryDto> output = new ArrayList<>();
        for (NewHistoryEntity historyEntity : historyRepository.findByRequesterId(requesterId)) {
            if (historyEntity.getItem().getStuff().getDepartment().getId() == departmentId) {
                output.add(historyEntity.toHistoryDto());
            }
        }

        return output;
    }

    @Override
    public HistoryDto getById(@NonNull UUID historyId) {
        return findHistoryEntity(historyId).toHistoryDto();
    }

    @Override
    public HistoryDto create(
            @NonNull UUID historyId, @NonNull UUID itemId, int num, UUID requesterId,
            UUID approveManagerId, UUID returnManagerId, UUID lostManagerId,
            UUID cancelManagerId, long requestedAt, long approvedAt, long returnedAt,
            long lostAt, long canceledAt
    ) {
        NewItemEntity itemOfNewHistory = getItemEntityOrThrowInvalidIndexException(itemId);

        checkHistoryIdConflict(historyId);
        checkHistoryConflict(itemId, num);

        NewHistoryEntity newHistoryEntity = new NewHistoryEntity(
                historyId,
                itemOfNewHistory,
                num,
                getUserEntityIfIdIsNotNull(requesterId),
                getUserEntityIfIdIsNotNull(approveManagerId),
                getUserEntityIfIdIsNotNull(returnManagerId),
                getUserEntityIfIdIsNotNull(lostManagerId),
                getUserEntityIfIdIsNotNull(cancelManagerId),
                requestedAt,
                approvedAt,
                returnedAt,
                lostAt,
                canceledAt
        );
        return historyRepository.save(newHistoryEntity).toHistoryDto();
    }

    @Override
    public HistoryDto update(
            @NonNull UUID historyId, @NonNull UUID itemId, int num, UUID requesterId,
            UUID approveManagerId, UUID returnManagerId, UUID lostManagerId,
            UUID cancelManagerId, long requestedAt, long approvedAt, long returnedAt,
            long lostAt, long canceledAt
    ) {
        NewHistoryEntity target = findHistoryEntity(historyId);
        NewItemEntity itemOfNewHistory = getItemEntityOrThrowInvalidIndexException(itemId);

        if (doesIndexChange(target, itemId, num)) {
            checkHistoryConflict(itemOfNewHistory.getId(), num);
        }

        target = target.withItem(itemOfNewHistory)
                .withNum(num)
                .withRequester(getUserEntityIfIdIsNotNull(requesterId))
                .withApproveManager(getUserEntityIfIdIsNotNull(approveManagerId))
                .withReturnManager(getUserEntityIfIdIsNotNull(returnManagerId))
                .withLostManager(getUserEntityIfIdIsNotNull(lostManagerId))
                .withCancelManager(getUserEntityIfIdIsNotNull(cancelManagerId))
                .withRequestedAt(requestedAt)
                .withApprovedAt(approvedAt)
                .withReturnedAt(returnedAt)
                .withLostAt(lostAt)
                .withCanceledAt(canceledAt);
        return historyRepository.save(target).toHistoryDto();
    }

    private List<HistoryDto> toHistoryDtoList(Iterable<NewHistoryEntity> historyEntities) {
        ArrayList<HistoryDto> output = new ArrayList<>();

        for (NewHistoryEntity historyEntity : historyEntities) {
            output.add(historyEntity.toHistoryDto());
        }
        return output;
    }

    private NewUserEntity getUserEntityIfIdIsNotNull(UUID userId) {
        if (userId == null) {
            return null;
        }
        return getUserEntityOrThrowInvalidIndexException(userId);
    }

    private boolean doesIndexChange(NewHistoryEntity target, UUID newItemId, int newHistoryNum) {
        return !(target.getItemId().equals(newItemId) && target.getNum() == newHistoryNum);
    }

    private void checkHistoryIdConflict(UUID historyId) {
        if (historyRepository.existsById(historyId)) {
            throw new ConflictException();
        }
    }

    private void checkHistoryConflict(UUID itemId, int historyNum) {
        if (historyRepository.existsByItemIdAndNum(itemId, historyNum)) {
            throw new ConflictException();
        }
    }
}
