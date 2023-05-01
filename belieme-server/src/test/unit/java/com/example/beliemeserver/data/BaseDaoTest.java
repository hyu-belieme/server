package com.example.beliemeserver.data;

import com.example.beliemeserver.data.entity._new.*;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dto._new.*;
import com.example.beliemeserver.util.EntityStubData;
import com.example.beliemeserver.util.RandomGetter;
import org.mockito.Mock;

import java.util.List;

public abstract class BaseDaoTest {
    protected EntityStubData stub = new EntityStubData();

    @Mock
    protected NewUniversityRepository univRepository;
    @Mock
    protected NewMajorRepository majorRepository;
    @Mock
    protected NewDepartmentRepository deptRepository;
    @Mock
    protected NewMajorDepartmentJoinRepository majorDeptJoinRepository;
    @Mock
    protected NewUserRepository userRepository;
    @Mock
    protected NewAuthorityRepository authRepository;
    @Mock
    protected NewAuthorityUserJoinRepository authUserJoinRepository;
    @Mock
    protected NewStuffRepository stuffRepository;
    @Mock
    protected NewItemRepository itemRepository;
    @Mock
    protected NewHistoryRepository historyRepository;

    protected RandomGetter<NewUniversityEntity> allUnivs() {
        return new RandomGetter<>(stub.ALL_UNIVS);
    }

    protected RandomGetter<NewMajorEntity> allMajors() {
        return new RandomGetter<>(stub.ALL_MAJORS);
    }

    protected RandomGetter<NewDepartmentEntity> allDepts() {
        return new RandomGetter<>(stub.ALL_DEPTS);
    }

    protected RandomGetter<NewAuthorityEntity> allAuths() {
        return new RandomGetter<>(stub.ALL_AUTHS);
    }

    protected RandomGetter<NewUserEntity> allUsers() {
        return new RandomGetter<>(stub.ALL_USERS);
    }

    protected RandomGetter<NewStuffEntity> allStuffs() {
        return new RandomGetter<>(stub.ALL_STUFFS);
    }

    protected RandomGetter<NewItemEntity> allItems() {
        return new RandomGetter<>(stub.ALL_ITEMS);
    }

    protected RandomGetter<NewHistoryEntity> allHistories() {
        return new RandomGetter<>(stub.ALL_HISTORIES);
    }

    protected <T> RandomGetter<T> withExclude(RandomGetter<T> rs, List<T> exclude) {
        return rs.filter((element) -> !exclude.contains(element));
    }

    protected <T> T randomSelectAndLog(RandomGetter<T> rs) {
        T output = rs.randomSelect();
        System.out.println(output);
        return output;
    }

    protected RandomGetter<NewStuffEntity> stuffsOnDept(RandomGetter<NewStuffEntity> rs, NewDepartmentEntity dept) {
        return rs.filter((stuff) -> stuff.getDepartment().getId().equals(dept.getId()));
    }

    protected RandomGetter<NewItemEntity> itemsOnDept(RandomGetter<NewItemEntity> rs, NewDepartmentEntity dept) {
        return rs.filter((item) -> item.getStuff().getDepartment().getId().equals(dept.getId()));
    }

    protected RandomGetter<NewHistoryEntity> historiesOnDept(RandomGetter<NewHistoryEntity> rs, NewDepartmentEntity dept) {
        return rs.filter((history) -> history.getItem().getStuff().getDepartment().getId().equals(dept.getId()));
    }

    protected NewUniversityEntity randomUniv() {
        return randomSelectAndLog(allUnivs());
    }

    protected NewDepartmentEntity randomDept() {
        return randomSelectAndLog(allDepts());
    }

    protected NewUserEntity randomUser() {
        return randomSelectAndLog(allUsers());
    }

    protected NewStuffEntity randomStuffOnDept(NewDepartmentEntity dept) {
        return randomSelectAndLog(stuffsOnDept(allStuffs(), dept));
    }

    protected NewItemEntity randomItemOnDept(NewDepartmentEntity dept) {
        return randomSelectAndLog(itemsOnDept(allItems(), dept));
    }

    protected NewHistoryEntity randomHistoryOnDept(NewDepartmentEntity dept) {
        return randomSelectAndLog(historiesOnDept(allHistories(), dept));
    }

    protected List<UniversityDto> toUniversityDtoList(List<NewUniversityEntity> universityEntityList) {
        return universityEntityList.stream().map(NewUniversityEntity::toUniversityDto).toList();
    }

    protected List<MajorDto> toMajorDtoList(List<NewMajorEntity> majorEntityList) {
        return majorEntityList.stream().map(NewMajorEntity::toMajorDto).toList();
    }

    protected List<DepartmentDto> toDepartmentDtoList(List<NewDepartmentEntity> departmentEntityList) {
        return departmentEntityList.stream().map(NewDepartmentEntity::toDepartmentDto).toList();
    }

    protected List<AuthorityDto> toAuthorityDtoList(List<NewAuthorityEntity> authorityEntityList) {
        return authorityEntityList.stream().map(NewAuthorityEntity::toAuthorityDto).toList();
    }

    protected List<UserDto> toUserDtoList(List<NewUserEntity> userEntityList) {
        return userEntityList.stream().map(NewUserEntity::toUserDto).toList();
    }

    protected List<StuffDto> toStuffDtoList(List<NewStuffEntity> stuffEntityList) {
        return stuffEntityList.stream().map(NewStuffEntity::toStuffDto).toList();
    }

    protected List<ItemDto> toItemDtoList(List<NewItemEntity> itemEntityList) {
        return itemEntityList.stream().map(NewItemEntity::toItemDto).toList();
    }

    protected List<HistoryDto> toHistoryDtoList(List<NewHistoryEntity> historyEntityList) {
        return historyEntityList.stream().map(NewHistoryEntity::toHistoryDto).toList();
    }
}
