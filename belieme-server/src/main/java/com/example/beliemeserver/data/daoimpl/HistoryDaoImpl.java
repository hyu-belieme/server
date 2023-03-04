package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.*;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.model.dao.HistoryDao;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryDaoImpl extends BaseDaoImpl implements HistoryDao {
    public HistoryDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<HistoryDto> getAllList() {
        return toHistoryDtoList(historyRepository.findAll());
    }

    @Override
    public List<HistoryDto> getListByDepartment(String universityCode, String departmentCode) {
        List<HistoryDto> output = new ArrayList<>();
        DepartmentEntity targetDepartment = findDepartmentEntity(universityCode, departmentCode);

        for (StuffEntity stuff : stuffRepository.findByDepartmentId(targetDepartment.getId())) {
            for (ItemEntity item : itemRepository.findByStuffId(stuff.getId())) {
                output.addAll(toHistoryDtoList(historyRepository.findByItemId(item.getId())));
            }
        }
        return output;
    }

    @Override
    public List<HistoryDto> getListByStuff(String universityCode, String departmentCode, String stuffName) {
        List<HistoryDto> output = new ArrayList<>();
        StuffEntity targetStuff = findStuffEntity(universityCode, departmentCode, stuffName);

        for (ItemEntity item : itemRepository.findByStuffId(targetStuff.getId())) {
            output.addAll(toHistoryDtoList(historyRepository.findByItemId(item.getId())));
        }
        return output;
    }

    @Override
    public List<HistoryDto> getListByItem(String universityCode, String departmentCode, String stuffName, int itemNum) {
        ItemEntity targetItem = findItemEntity(universityCode, departmentCode, stuffName, itemNum);
        return toHistoryDtoList(historyRepository.findByItemId(targetItem.getId()));
    }

    @Override
    public List<HistoryDto> getListByDepartmentAndRequester(String universityCodeForDepartment, String departmentCode, String universityCodeForUser, String requesterStudentId) {
        List<HistoryDto> output = new ArrayList<>();
        DepartmentEntity targetDepartment = findDepartmentEntity(universityCodeForDepartment, departmentCode);
        UserEntity targetRequester = findUserEntity(universityCodeForUser, requesterStudentId);

        for (HistoryEntity historyEntity : historyRepository.findByRequesterId(targetRequester.getId())) {
            if (historyEntity.getItem().getStuff().getDepartment().getId() == targetDepartment.getId()) {
                output.add(historyEntity.toHistoryDto());
            }
        }

        return output;
    }

    @Override
    public HistoryDto getByIndex(String universityCode, String departmentCode, String stuffName, int itemNum, int historyNum) {
        return findHistoryEntity(universityCode, departmentCode, stuffName, itemNum, historyNum).toHistoryDto();
    }

    @Override
    public HistoryDto create(HistoryDto newHistory) {
        ItemEntity itemOfNewHistory = findItemEntity(newHistory.item());

        checkHistoryConflict(itemOfNewHistory.getId(), newHistory.num());

        HistoryEntity newHistoryEntity = new HistoryEntity(
                itemOfNewHistory,
                newHistory.num(),
                toUserEntityOrNull(newHistory.requester()),
                toUserEntityOrNull(newHistory.approveManager()),
                toUserEntityOrNull(newHistory.returnManager()),
                toUserEntityOrNull(newHistory.lostManager()),
                toUserEntityOrNull(newHistory.cancelManager()),
                newHistory.reservedTimeStamp(),
                newHistory.approveTimeStamp(),
                newHistory.returnTimeStamp(),
                newHistory.lostTimeStamp(),
                newHistory.cancelTimeStamp()
        );
        return historyRepository.save(newHistoryEntity).toHistoryDto();
    }

    @Override
    public HistoryDto update(String universityCode, String departmentCode, String stuffName, int itemNum, int historyNum, HistoryDto newHistory) {
        HistoryEntity target = findHistoryEntity(universityCode, departmentCode, stuffName, itemNum, historyNum);
        ItemEntity itemOfNewHistory = findItemEntity(newHistory.item());

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
                .setReservedTimeStamp(newHistory.reservedTimeStamp())
                .setApproveTimeStamp(newHistory.approveTimeStamp())
                .setReturnTimeStamp(newHistory.returnTimeStamp())
                .setLostTimeStamp(newHistory.lostTimeStamp())
                .setCancelTimeStamp(newHistory.cancelTimeStamp());
        return target.toHistoryDto();
    }

    private List<HistoryDto> toHistoryDtoList(Iterable<HistoryEntity> historyEntities) {
        ArrayList<HistoryDto> output = new ArrayList<>();

        for (HistoryEntity historyEntity : historyEntities) {
            output.add(historyEntity.toHistoryDto());
        }
        return output;
    }

    private boolean doesIndexChange(HistoryEntity target, HistoryDto newHistory) {
        String oldUniversityCode = target.getItem().getStuff().getDepartment().getUniversity().getCode();
        String oldDepartmentCode = target.getItem().getStuff().getDepartment().getCode();
        String oldStuffName = target.getItem().getStuff().getName();
        int oldItemNum = target.getItem().getNum();
        int oldHistoryNum = target.getNum();

        return !(oldUniversityCode.equals(newHistory.item().stuff().department().university().code())
                && oldDepartmentCode.equals(newHistory.item().stuff().department().code())
                && oldStuffName.equals(newHistory.item().stuff().name())
                && oldItemNum == newHistory.item().num()
                && oldHistoryNum == newHistory.num());
    }

    private void checkHistoryConflict(int itemId, int historyNum) {
        if (historyRepository.existsByItemIdAndNum(itemId, historyNum)) {
            throw new ConflictException();
        }
    }

    private UserEntity toUserEntityOrNull(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return findUserEntity(userDto);
    }
}
