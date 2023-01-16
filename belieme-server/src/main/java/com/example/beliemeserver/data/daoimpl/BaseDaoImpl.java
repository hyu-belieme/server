package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.daoimpl.util.IndexAdapter;
import com.example.beliemeserver.data.entity.*;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseDaoImpl {
    protected final UniversityRepository universityRepository;
    protected final DepartmentRepository departmentRepository;
    protected final UserRepository userRepository;
    protected final MajorRepository majorRepository;
    protected final MajorUserJoinRepository majorUserJoinRepository;
    protected final MajorDepartmentJoinRepository majorDepartmentJoinRepository;
    protected final AuthorityRepository authorityRepository;
    protected final AuthorityUserJoinRepository authorityUserJoinRepository;
    protected final StuffRepository stuffRepository;
    protected final ItemRepository itemRepository;
    protected final HistoryRepository historyRepository;

    @Autowired
    public BaseDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        this.universityRepository = universityRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.majorRepository = majorRepository;
        this.majorUserJoinRepository = majorUserJoinRepository;
        this.majorDepartmentJoinRepository = majorDepartmentJoinRepository;
        this.authorityRepository = authorityRepository;
        this.authorityUserJoinRepository = authorityUserJoinRepository;
        this.stuffRepository = stuffRepository;
        this.itemRepository = itemRepository;
        this.historyRepository = historyRepository;
    }

    protected UniversityEntity getUniversityEntity(String universityCode) throws NotFoundException {
        return IndexAdapter.getUniversityEntity(universityRepository, universityCode);
    }

    protected UniversityEntity getUniversityEntity(UniversityDto universityDto) throws NotFoundException {
        String universityCode = universityDto.code();
        return getUniversityEntity(universityCode);
    }

    protected DepartmentEntity getDepartmentEntity(String universityCode, String departmentCode) throws NotFoundException {
        int universityId = getUniversityEntity(universityCode).getId();

        return IndexAdapter.getDepartmentEntity(departmentRepository, universityId, departmentCode);
    }

    protected DepartmentEntity getDepartmentEntity(DepartmentDto departmentDto) throws NotFoundException {
        String universityCode = departmentDto.university().code();
        String departmentCode = departmentDto.code();

        return getDepartmentEntity(universityCode, departmentCode);
    }

    protected UserEntity getUserEntity(String universityCode, String studentId) throws NotFoundException {
        int universityId = getUniversityEntity(universityCode).getId();

        return IndexAdapter.getUserEntity(userRepository, universityId, studentId);
    }

    protected UserEntity getUserEntity(UserDto userDto) throws NotFoundException {
        String universityCode = userDto.university().code();
        String studentId = userDto.studentId();

        return getUserEntity(universityCode, studentId);
    }

    protected UserEntity getUserEntityByToken(String token) throws NotFoundException {
        return IndexAdapter.getUserEntityByToken(userRepository, token);
    }

    protected MajorEntity getMajorEntity(String universityCode, String majorCode) throws NotFoundException {
        int universityId = getUniversityEntity(universityCode).getId();

        return IndexAdapter.getMajorEntity(majorRepository, universityId, majorCode);
    }

    protected MajorEntity getMajorEntity(MajorDto majorDto) throws NotFoundException {
        String universityCode = majorDto.university().code();
        String majorCode = majorDto.code();

        return getMajorEntity(universityCode, majorCode);
    }

    protected AuthorityEntity getAuthorityEntity(String universityCode, String departmentCode, String permission) throws NotFoundException {
        int departmentId = getDepartmentEntity(universityCode, departmentCode).getId();

        return IndexAdapter.getAuthorityEntity(authorityRepository, departmentId, permission);
    }

    protected AuthorityEntity getAuthorityEntity(AuthorityDto authorityDto) throws NotFoundException {
        String universityCode = authorityDto.department().university().code();
        String departmentCode = authorityDto.department().code();
        String permission = authorityDto.permission().toString();

        return getAuthorityEntity(universityCode, departmentCode, permission);
    }

    protected StuffEntity getStuffEntity(String universityCode, String departmentCode, String stuffName) throws NotFoundException {
        int departmentId = getDepartmentEntity(universityCode, departmentCode).getId();

        return IndexAdapter.getStuffEntity(stuffRepository, departmentId, stuffName);
    }

    protected StuffEntity getStuffEntity(StuffDto stuffDto) throws NotFoundException {
        String universityCode = stuffDto.department().university().code();
        String departmentCode = stuffDto.department().code();
        String stuffName = stuffDto.name();

        return getStuffEntity(universityCode, departmentCode, stuffName);
    }

    protected ItemEntity getItemEntity(String universityCode, String departmentCode, String stuffName, int itemNum) throws NotFoundException {
        int stuffId = getStuffEntity(universityCode, departmentCode, stuffName).getId();

        return IndexAdapter.getItemEntity(itemRepository, stuffId, itemNum);
    }

    protected ItemEntity getItemEntity(ItemDto itemDto) throws NotFoundException {
        String universityCode = itemDto.stuff().department().university().code();
        String departmentCode = itemDto.stuff().department().code();
        String stuffName = itemDto.stuff().name();
        int itemNum = itemDto.num();

        return getItemEntity(universityCode, departmentCode, stuffName, itemNum);
    }

    protected HistoryEntity getHistoryEntity(String universityCode, String departmentCode, String stuffName, int itemNum, int historyNum) throws NotFoundException {
        int itemId = getItemEntity(universityCode, departmentCode, stuffName, itemNum).getId();

        return IndexAdapter.getHistoryEntity(historyRepository, itemId, historyNum);
    }

    protected HistoryEntity getHistoryEntity(HistoryDto historyDto) throws NotFoundException {
        String universityCode = historyDto.item().stuff().department().university().code();
        String departmentCode = historyDto.item().stuff().department().code();
        String stuffName = historyDto.item().stuff().name();
        int itemNum = historyDto.item().num();
        int historyNum = historyDto.num();

        return getHistoryEntity(universityCode, departmentCode, stuffName, itemNum, historyNum);
    }
}
