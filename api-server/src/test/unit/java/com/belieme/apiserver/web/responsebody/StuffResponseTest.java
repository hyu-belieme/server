package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.util.RandomGetter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;


public class StuffResponseTest extends BaseResponseTest {

  private final RandomGetter<StuffDto> stuffGetter = new RandomGetter<>(stub.ALL_STUFFS);

  @RepeatedTest(10)
  @DisplayName("[-]_[`stuff json serialization` 테스트]_[-]")
  public void stuffJsonSerializationTest() throws IOException, ParseException {
    StuffDto stuff = stuffGetter.randomSelect();

    JSONObject json = makeJsonObject(StuffResponse.from(stuff));
    stuffJsonCmpAssertions(json, stuff);
  }

  @RepeatedTest(10)
  @DisplayName("[-]_[`university`와 `department`를 제외한 `stuff json serialization` 테스트]_[-]")
  public void stuffJsonWithoutUnivAndDeptSerializationTest() throws IOException, ParseException {
    StuffDto stuff = stuffGetter.randomSelect();

    JSONObject json = makeJsonObject(StuffResponse.from(stuff).withoutUniversityAndDepartment());
    stuffJsonWithoutUnivAndDeptInfoCmpAssertions(json, stuff);
  }

  @RepeatedTest(10)
  @DisplayName("[-]_[`items`를 제외한 `stuff json serialization` 테스트]_[-]")
  public void stuffJsonWithoutItemsSerializationTest() throws IOException, ParseException {
    StuffDto stuff = stuffGetter.randomSelect();

    JSONObject json = makeJsonObject(StuffResponse.from(stuff).withoutItems());
    stuffJsonWithoutItemsCmpAssertions(json, stuff);
  }
}