package com.belieme.apiserver.data;

import static org.mockito.ArgumentMatchers.argThat;

import com.belieme.apiserver.data.entity.AuthorityEntity;
import com.belieme.apiserver.data.entity.AuthorityUserJoinEntity;
import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.entity.ItemEntity;
import com.belieme.apiserver.data.entity.MajorDepartmentJoinEntity;
import com.belieme.apiserver.data.entity.MajorEntity;
import com.belieme.apiserver.data.entity.StuffEntity;
import com.belieme.apiserver.data.entity.UniversityEntity;
import com.belieme.apiserver.data.entity.UserEntity;
import com.belieme.apiserver.data.repository.AuthorityRepository;
import com.belieme.apiserver.data.repository.AuthorityUserJoinRepository;
import com.belieme.apiserver.data.repository.DepartmentRepository;
import com.belieme.apiserver.data.repository.HistoryRepository;
import com.belieme.apiserver.data.repository.ItemRepository;
import com.belieme.apiserver.data.repository.MajorDepartmentJoinRepository;
import com.belieme.apiserver.data.repository.MajorRepository;
import com.belieme.apiserver.data.repository.StuffRepository;
import com.belieme.apiserver.data.repository.UniversityRepository;
import com.belieme.apiserver.data.repository.UserRepository;
import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.MajorDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.util.ArgumentMatchHelper;
import com.belieme.apiserver.util.EntityStubData;
import com.belieme.apiserver.util.RandomGetter;
import java.util.List;
import org.mockito.Mock;

public abstract class BaseDaoTest {

  protected EntityStubData stub = new EntityStubData();

  @Mock
  protected UniversityRepository univRepository;
  @Mock
  protected MajorRepository majorRepository;
  @Mock
  protected DepartmentRepository deptRepository;
  @Mock
  protected MajorDepartmentJoinRepository majorDeptJoinRepository;
  @Mock
  protected UserRepository userRepository;
  @Mock
  protected AuthorityRepository authRepository;
  @Mock
  protected AuthorityUserJoinRepository authUserJoinRepository;
  @Mock
  protected StuffRepository stuffRepository;
  @Mock
  protected ItemRepository itemRepository;
  @Mock
  protected HistoryRepository historyRepository;

  protected RandomGetter<UniversityEntity> allUnivs() {
    return new RandomGetter<>(stub.ALL_UNIVS);
  }

  protected RandomGetter<MajorEntity> allMajors() {
    return new RandomGetter<>(stub.ALL_MAJORS);
  }

  protected RandomGetter<DepartmentEntity> allDepts() {
    return new RandomGetter<>(stub.ALL_DEPTS);
  }

  protected RandomGetter<AuthorityEntity> allAuths() {
    return new RandomGetter<>(stub.ALL_AUTHS);
  }

  protected RandomGetter<UserEntity> allUsers() {
    return new RandomGetter<>(stub.ALL_USERS);
  }

  protected RandomGetter<StuffEntity> allStuffs() {
    return new RandomGetter<>(stub.ALL_STUFFS);
  }

  protected RandomGetter<ItemEntity> allItems() {
    return new RandomGetter<>(stub.ALL_ITEMS);
  }

  protected RandomGetter<HistoryEntity> allHistories() {
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

  protected RandomGetter<DepartmentEntity> deptsOnUniv(RandomGetter<DepartmentEntity> rs,
      UniversityEntity univ) {
    return rs.filter((dept) -> dept.getUniversity().getId().equals(univ.getId()));
  }

  protected UniversityEntity randomUniv() {
    return randomSelectAndLog(allUnivs());
  }

  protected DepartmentEntity randomDept() {
    return randomSelectAndLog(allDepts());
  }

  protected DepartmentEntity randomDeptOnUniv(UniversityEntity univ) {
    return randomSelectAndLog(deptsOnUniv(allDepts(), univ));
  }

  protected MajorEntity randomMajor() {
    return randomSelectAndLog(allMajors());
  }

  protected UserEntity randomUser() {
    return randomSelectAndLog(allUsers());
  }

  protected AuthorityEntity randomAuth() {
    return randomSelectAndLog(allAuths());
  }

  protected StuffEntity randomStuff() {
    return randomSelectAndLog(allStuffs());
  }

  protected ItemEntity randomItem() {
    return randomSelectAndLog(allItems());
  }

  protected HistoryEntity randomHistory() {
    return randomSelectAndLog(allHistories());
  }

  protected List<UniversityDto> toUniversityDtoList(List<UniversityEntity> universityEntityList) {
    return universityEntityList.stream().map(UniversityEntity::toUniversityDto).toList();
  }

  protected List<MajorDto> toMajorDtoList(List<MajorEntity> majorEntityList) {
    return majorEntityList.stream().map(MajorEntity::toMajorDto).toList();
  }

  protected List<DepartmentDto> toDepartmentDtoList(List<DepartmentEntity> departmentEntityList) {
    return departmentEntityList.stream().map(DepartmentEntity::toDepartmentDto).toList();
  }

  protected List<AuthorityDto> toAuthorityDtoList(List<AuthorityEntity> authorityEntityList) {
    return authorityEntityList.stream().map(AuthorityEntity::toAuthorityDto).toList();
  }

  protected List<UserDto> toUserDtoList(List<UserEntity> userEntityList) {
    return userEntityList.stream().map(UserEntity::toUserDto).toList();
  }

  protected List<StuffDto> toStuffDtoList(List<StuffEntity> stuffEntityList) {
    return stuffEntityList.stream().map(StuffEntity::toStuffDto).toList();
  }

  protected List<ItemDto> toItemDtoList(List<ItemEntity> itemEntityList) {
    return itemEntityList.stream().map(ItemEntity::toItemDto).toList();
  }

  protected List<HistoryDto> toHistoryDtoList(List<HistoryEntity> historyEntityList) {
    return historyEntityList.stream().map(HistoryEntity::toHistoryDto).toList();
  }

  protected UniversityEntity mockArg(UniversityEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected DepartmentEntity mockArg(DepartmentEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected MajorEntity mockArg(MajorEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected MajorDepartmentJoinEntity mockArg(MajorDepartmentJoinEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected UserEntity mockArg(UserEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected AuthorityEntity mockArg(AuthorityEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected AuthorityUserJoinEntity mockArg(AuthorityUserJoinEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected StuffEntity mockArg(StuffEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected ItemEntity mockArg(ItemEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }

  protected HistoryEntity mockArg(HistoryEntity target) {
    return argThat(e -> ArgumentMatchHelper.canMatch(target, e));
  }
}
