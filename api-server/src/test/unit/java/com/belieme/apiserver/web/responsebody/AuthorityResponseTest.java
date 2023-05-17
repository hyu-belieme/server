package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.util.RandomGetter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;


public class AuthorityResponseTest extends BaseResponseTest {

  private final RandomGetter<DepartmentDto> deptGetter = new RandomGetter<>(stub.ALL_DEPTS);
  private final RandomGetter<Permission> permissionGetter = new RandomGetter<>(
      stub.ALL_PERMISSIONS);

  @RepeatedTest(10)
  @DisplayName("[-]_[`authority json serialization` 테스트]_[-]")
  public void departmentJsonSerializationTest() throws IOException, ParseException {
    AuthorityDto auth = new AuthorityDto(deptGetter.randomSelect(),
        permissionGetter.randomSelect());

    JSONObject json = makeJsonObject(AuthorityResponse.from(auth));

    authorityJsonCmpAssertions(json, auth);
  }
}