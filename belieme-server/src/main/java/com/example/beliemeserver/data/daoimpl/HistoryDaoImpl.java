package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.*;
import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dao.HistoryDao;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryDaoImpl extends BaseDaoImpl implements HistoryDao {
    public HistoryDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorUserJoinRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    @Transactional
    public List<HistoryDto> getAllList() throws DataException {
        return toHistoryDto(historyRepository.findAll());
    }

    @Override
    @Transactional
    public List<HistoryDto> getListByDepartment(String universityCode, String departmentCode) throws DataException, NotFoundException {
        List<HistoryDto> output = new ArrayList<>();
        DepartmentEntity targetDepartment = getDepartmentEntity(universityCode, departmentCode);

        for(StuffEntity stuff : stuffRepository.findByDepartmentId(targetDepartment.getId())) {
            for(ItemEntity item : itemRepository.findByStuffId(stuff.getId())) {
                output.addAll(toHistoryDto(historyRepository.findByItemId(item.getId())));
            }
        }
        return output;
    }

    @Override
    @Transactional
    public List<HistoryDto> getListByDepartmentAndRequester(String universityCodeForDepartment, String departmentCode, String universityCodeForUser, String requesterStudentId) throws DataException, NotFoundException {
        List<HistoryDto> output = new ArrayList<>();
        DepartmentEntity targetDepartment = getDepartmentEntity(universityCodeForDepartment, departmentCode);
        UserEntity targetRequester = getUserEntity(universityCodeForUser, requesterStudentId);

        for(HistoryEntity historyEntity : historyRepository.findByRequesterId(targetRequester.getId())) {
            if(historyEntity.getItem().getStuff().getDepartment().getId() == targetDepartment.getId()) {
                output.add(historyEntity.toHistoryDto());
            }
        }

        return output;
    }

    @Override
    @Transactional
    public HistoryDto getByIndex(String universityCode, String departmentCode, String stuffName, int itemNum, int historyNum) throws NotFoundException, DataException {
        return getHistoryEntity(universityCode, departmentCode, stuffName, itemNum, historyNum).toHistoryDto();
    }

    @Override
    @Transactional
    public HistoryDto create(HistoryDto newHistory) throws ConflictException, NotFoundException, DataException {
        ItemEntity itemOfNewHistory = getItemEntity(newHistory.item());

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
    @Transactional
    public HistoryDto update(String universityCode, String departmentCode, String stuffName, int itemNum, int historyNum, HistoryDto newHistory) throws NotFoundException, DataException, ConflictException {
        HistoryEntity target = getHistoryEntity(universityCode, departmentCode, stuffName, itemNum, historyNum);
        ItemEntity itemOfNewHistory = getItemEntity(newHistory.item());

        if(doesIndexChange(target, newHistory)) {
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

    private void checkHistoryConflict(int itemId, int historyNum) throws ConflictException {
        if(historyRepository.existsByItemIdAndNum(itemId, historyNum)) {
            throw new ConflictException();
        }
    }

    private UserEntity toUserEntityOrNull(UserDto userDto) throws NotFoundException {
        if(userDto == null) {
            return null;
        }
        return getUserEntity(userDto);
    }

    private boolean doesIndexChange(HistoryEntity target, HistoryDto newHistory) {
        String oldUniversityCode = target.getItem().getStuff().getDepartment().getUniversity().getCode();
        String oldDepartmentCode = target.getItem().getStuff().getDepartment().getCode();
        String oldStuffName = target.getItem().getStuff().getName();
        int oldItemNum = target.getItem().getNum();
        int oldHistoryNum = target.getNum();

        String newUniversityCode = newHistory.item().stuff().department().university().code();
        String newDepartmentCode = newHistory.item().stuff().department().code();
        String newName = newHistory.item().stuff().name();
        int newItemNum = newHistory.item().num();
        int newHistoryNum = newHistory.num();

        return !(oldUniversityCode.equals(newUniversityCode)
                && oldDepartmentCode.equals(newDepartmentCode)
                && oldStuffName.equals(newName)
                && oldItemNum == newItemNum
                && oldHistoryNum == newHistoryNum);
    }

    private List<HistoryDto> toHistoryDto(Iterable<HistoryEntity> historyEntities) throws FormatDoesNotMatchException {
        ArrayList<HistoryDto> output = new ArrayList<>();

        for(HistoryEntity historyEntity : historyEntities) {
            output.add(historyEntity.toHistoryDto());
        }
        return output;
    }
}
