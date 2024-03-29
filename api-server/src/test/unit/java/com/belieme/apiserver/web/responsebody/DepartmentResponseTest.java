package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.util.RandomGetter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;


public class DepartmentResponseTest extends BaseResponseTest {

  private final RandomGetter<DepartmentDto> deptGetter = new RandomGetter<>(stub.ALL_DEPTS);

  @RepeatedTest(10)
  @DisplayName("[-]_[`department json serialization` 테스트]_[-]")
  public void departmentJsonSerializationTest() throws IOException, ParseException {
    DepartmentDto dept = deptGetter.randomSelect();

    JSONObject json = makeJsonObject(DepartmentResponse.from(dept));
    basicDeptJsonCmpAssertions(json, dept);
  }

  @RepeatedTest(10)
  @DisplayName("[-]_[`university` 제외한 `department json serialization` 테스트]_[-]")
  public void departmentWithoutUniversityJsonSerializationTest()
      throws IOException, ParseException {
    DepartmentDto dept = deptGetter.randomSelect();

    JSONObject json = makeJsonObject(DepartmentResponse.from(dept).withoutUniversity());
    deptWithoutUnivJsonCmpAssertions(json, dept);
  }
}