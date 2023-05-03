package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewHistoryEntity;
import com.example.beliemeserver.data.entity._new.NewItemEntity;
import com.example.beliemeserver.data.entity._new.NewStuffEntity;
import com.example.beliemeserver.data.entity._new.NewUserEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.HistoryDao;
import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
import com.example.beliemeserver.error.exception.ConflictException;
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
    public List<HistoryDto> getListByDepartment(UUID departmentId) {
        List<HistoryDto> output = new ArrayList<>();
        for (NewStuffEntity stuff : stuffRepository.findByDepartmentId(departmentId)) {
            for (NewItemEntity item : itemRepository.findByStuffId(stuff.getId())) {
                output.addAll(toHistoryDtoList(historyRepository.findByItemId(item.getId())));
            }
        }
        return output;
    }

    @Override
    public List<HistoryDto> getListByStuff(UUID stuffId) {
        List<HistoryDto> output = new ArrayList<>();
        for (NewItemEntity item : itemRepository.findByStuffId(stuffId)) {
            output.addAll(toHistoryDtoList(historyRepository.findByItemId(item.getId())));
        }
        return output;
    }

    @Override
    public List<HistoryDto> getListByItem(UUID itemId) {
        return toHistoryDtoList(historyRepository.findByItemId(itemId));
    }

    @Override
    public List<HistoryDto> getListByDepartmentAndRequester(UUID departmentId, UUID requesterId) {
        List<HistoryDto> output = new ArrayList<>();
        for (NewHistoryEntity historyEntity : historyRepository.findByRequesterId(requesterId)) {
            if (historyEntity.getItem().getStuff().getDepartment().getId() == departmentId) {
                output.add(historyEntity.toHistoryDto());
            }
        }

        return output;
    }

    @Override
    public HistoryDto getById(UUID historyId) {
        return findHistoryEntity(historyId).toHistoryDto();
    }

    @Override
    public HistoryDto create(HistoryDto newHistory) {
        NewItemEntity itemOfNewHistory = findItemEntity(newHistory.item().id());

        checkHistoryConflict(itemOfNewHistory.getId(), newHistory.num());

        NewHistoryEntity newHistoryEntity = new NewHistoryEntity(
                newHistory.id(),
                itemOfNewHistory,
                newHistory.num(),
                toUserEntityOrNull(newHistory.requester()),
                toUserEntityOrNull(newHistory.approveManager()),
                toUserEntityOrNull(newHistory.returnManager()),
                toUserEntityOrNull(newHistory.lostManager()),
                toUserEntityOrNull(newHistory.cancelManager()),
                newHistory.requestedAt(),
                newHistory.approvedAt(),
                newHistory.returnedAt(),
                newHistory.lostAt(),
                newHistory.canceledAt()
        );
        return historyRepository.save(newHistoryEntity).toHistoryDto();
    }

    @Override
    public HistoryDto update(UUID historyId, HistoryDto newHistory) {
        NewHistoryEntity target = findHistoryEntity(historyId);
        NewItemEntity itemOfNewHistory = findItemEntity(newHistory.item().id());

        if (doesIndexChange(target, newHistory)) {
            checkHistoryConflict(itemOfNewHistory.getId(), newHistory.num());
        }

        target = target.withItem(itemOfNewHistory)
                .withNum(newHistory.num())
                .withRequester(toUserEntityOrNull(newHistory.requester()))
                .withApproveManager(toUserEntityOrNull(newHistory.approveManager()))
                .withReturnManager(toUserEntityOrNull(newHistory.returnManager()))
                .withLostManager(toUserEntityOrNull(newHistory.lostManager()))
                .withCancelManager(toUserEntityOrNull(newHistory.cancelManager()))
                .withRequestedAt(newHistory.requestedAt())
                .withApprovedAt(newHistory.approvedAt())
                .withReturnedAt(newHistory.returnedAt())
                .withLostAt(newHistory.lostAt())
                .withCanceledAt(newHistory.canceledAt());
        return historyRepository.save(target).toHistoryDto();
    }

    private List<HistoryDto> toHistoryDtoList(Iterable<NewHistoryEntity> historyEntities) {
        ArrayList<HistoryDto> output = new ArrayList<>();

        for (NewHistoryEntity historyEntity : historyEntities) {
            output.add(historyEntity.toHistoryDto());
        }
        return output;
    }

    private boolean doesIndexChange(NewHistoryEntity target, HistoryDto newHistory) {
        String oldUniversityName = target.getItem().getStuff().getDepartment().getUniversity().getName();
        String oldDepartmentName = target.getItem().getStuff().getDepartment().getName();
        String oldStuffName = target.getItem().getStuff().getName();
        int oldItemNum = target.getItem().getNum();
        int oldHistoryNum = target.getNum();

        return !(oldUniversityName.equals(newHistory.item().stuff().department().university().name())
                && oldDepartmentName.equals(newHistory.item().stuff().department().name())
                && oldStuffName.equals(newHistory.item().stuff().name())
                && oldItemNum == newHistory.item().num()
                && oldHistoryNum == newHistory.num());
    }

    private void checkHistoryConflict(UUID itemId, int historyNum) {
        if (historyRepository.existsByItemIdAndNum(itemId, historyNum)) {
            throw new ConflictException();
        }
    }

    private NewUserEntity toUserEntityOrNull(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return findUserEntity(userDto.id());
    }
}
