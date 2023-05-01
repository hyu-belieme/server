package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.*;
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
    public List<HistoryDto> getListByDepartment(String universityName, String departmentName) {
        List<HistoryDto> output = new ArrayList<>();
        NewDepartmentEntity targetDepartment = findDepartmentEntity(universityName, departmentName);

        for (NewStuffEntity stuff : stuffRepository.findByDepartmentId(targetDepartment.getId())) {
            for (NewItemEntity item : itemRepository.findByStuffId(stuff.getId())) {
                output.addAll(toHistoryDtoList(historyRepository.findByItemId(item.getId())));
            }
        }
        return output;
    }

    @Override
    public List<HistoryDto> getListByStuff(String universityName, String departmentName, String stuffName) {
        List<HistoryDto> output = new ArrayList<>();
        NewStuffEntity targetStuff = findStuffEntity(universityName, departmentName, stuffName);

        for (NewItemEntity item : itemRepository.findByStuffId(targetStuff.getId())) {
            output.addAll(toHistoryDtoList(historyRepository.findByItemId(item.getId())));
        }
        return output;
    }

    @Override
    public List<HistoryDto> getListByItem(String universityName, String departmentName, String stuffName, int itemNum) {
        NewItemEntity targetItem = findItemEntity(universityName, departmentName, stuffName, itemNum);
        return toHistoryDtoList(historyRepository.findByItemId(targetItem.getId()));
    }

    @Override
    public List<HistoryDto> getListByDepartmentAndRequester(String universityNameForDepartment, String departmentName, String universityNameForUser, String requesterStudentId) {
        List<HistoryDto> output = new ArrayList<>();
        NewDepartmentEntity targetDepartment = findDepartmentEntity(universityNameForDepartment, departmentName);
        NewUserEntity targetRequester = findUserEntity(universityNameForUser, requesterStudentId);

        for (NewHistoryEntity historyEntity : historyRepository.findByRequesterId(targetRequester.getId())) {
            if (historyEntity.getItem().getStuff().getDepartment().getId() == targetDepartment.getId()) {
                output.add(historyEntity.toHistoryDto());
            }
        }

        return output;
    }

    @Override
    public HistoryDto getByIndex(String universityName, String departmentName, String stuffName, int itemNum, int historyNum) {
        return findHistoryEntity(universityName, departmentName, stuffName, itemNum, historyNum).toHistoryDto();
    }

    @Override
    public HistoryDto create(HistoryDto newHistory) {
        NewItemEntity itemOfNewHistory = findItemEntity(newHistory.item());

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
    public HistoryDto update(String universityName, String departmentName, String stuffName, int itemNum, int historyNum, HistoryDto newHistory) {
        NewHistoryEntity target = findHistoryEntity(universityName, departmentName, stuffName, itemNum, historyNum);
        NewItemEntity itemOfNewHistory = findItemEntity(newHistory.item());

        if (doesIndexChange(target, newHistory)) {
            checkHistoryConflict(itemOfNewHistory.getId(), newHistory.num());
        }

        target.setItem(itemOfNewHistory)
                .setNum(newHistory.num())
                .setRequester(toUserEntityOrNull(newHistory.requester()))
                .setApproveManager(toUserEntityOrNull(newHistory.approveManager()))
                .setReturnManager(toUserEntityOrNull(newHistory.returnManager()))
                .setLostManager(toUserEntityOrNull(newHistory.lostManager()))
                .setCancelManager(toUserEntityOrNull(newHistory.cancelManager()))
                .setRequestedAt(newHistory.requestedAt())
                .setApprovedAt(newHistory.approvedAt())
                .setReturnedAt(newHistory.returnedAt())
                .setLostAt(newHistory.lostAt())
                .setCanceledAt(newHistory.canceledAt());
        return target.toHistoryDto();
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
        return findUserEntity(userDto);
    }
}
