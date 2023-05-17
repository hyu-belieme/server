package com.belieme.apiserver.domain;

import static org.mockito.Mockito.when;

import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.domain.exception.PermissionDeniedException;
import com.belieme.apiserver.domain.service.UniversityService;
import com.belieme.apiserver.error.exception.NotFoundException;
import com.belieme.apiserver.util.TestHelper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UniversityServiceTest extends BaseServiceTest {

  @InjectMocks
  private UniversityService universityService;

  @Nested
  @DisplayName("getAllList()")
  public class TestGetAllList extends UnivNestedTest {

    @Override
    protected void setUpDefault() {
      setRequester(randomDevUser());
    }

    @Override
    protected List<UniversityDto> execMethod() {
      return universityService.getAllList(userToken);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void success() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(universityDao.getAllList()).thenReturn(stub.ALL_UNIVS);

      TestHelper.listCompareTest(this::execMethod, stub.ALL_UNIVS);
    }
  }

  @Nested
  @DisplayName("getById()")
  public class TestGetByIndex extends UnivNestedTest {

    private UniversityDto univ;
    private UUID univId;

    @Override
    protected void setUpDefault() {
      setRequester(randomDevUser());
      setUniv(randomUniv());
    }

    private void setUniv(UniversityDto univ) {
      this.univ = univ;
      this.univId = univ.id();
    }

    @Override
    protected UniversityDto execMethod() {
      return universityService.getById(userToken, univId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void success() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(universityDao.getById(univId)).thenReturn(univ);

      TestHelper.objectCompareTest(this::execMethod, univ);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[해당 code에 university가 존재하지 않을 시]_[NotFoundException]")
    public void universityNotFound_NotFoundException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(universityDao.getById(univId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
    }
  }

  private UniversityDto randomUniv() {
    return randomSelectAndLog(allUnivs());
  }

  private abstract class UnivNestedTest extends BaseNestedTest {

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    public void accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequester(randomNonDevUser());

      when(userDao.getByToken(userToken)).thenReturn(requester);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }
  }
}
