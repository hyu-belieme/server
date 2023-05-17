package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.util.RandomGetter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;


public class HistoryResponseTest extends BaseResponseTest {

  private final RandomGetter<HistoryDto> historyGetter = new RandomGetter<>(stub.ALL_HISTORIES);

  @RepeatedTest(10)
  @DisplayName("[-]_[`history json serialization` 테스트]_[-]")
  public void historyJsonSerializationTest() throws IOException, ParseException {
    HistoryDto history = historyGetter.randomSelect();

    JSONObject json = makeJsonObject(HistoryResponse.from(history));
    historyJsoCmpAssertions(json, history);
  }

  @RepeatedTest(10)
  @DisplayName("[-]_[`university`와 `department`를 제외한 `history json serialization` 테스트]_[-]")
  public void historyJsonWithoutUnivAndDeptSerializationTest() throws IOException, ParseException {
    HistoryDto history = historyGetter.randomSelect();

    JSONObject json = makeJsonObject(
        HistoryResponse.from(history).withoutUniversityAndDepartment());
    historyJsonWithoutUnivAndDeptInfoCmpAssertions(json, history);
  }

  @RepeatedTest(10)
  @DisplayName("[-]_[`item`을 제외한 `history json serialization` 테스트]_[-]")
  public void historyJsonWithoutStuffSerializationTest() throws IOException, ParseException {
    HistoryDto history = historyGetter.randomSelect();

    JSONObject json = makeJsonObject(HistoryResponse.from(history).withoutItem());
    historyJsonWithoutItemInfoCmpAssertions(json, history);
  }

  @RepeatedTest(10)
  @DisplayName("[-]_[`university`와 `department`, `item`을 제외한 `history json serialization` 테스트]_[-]")
  public void historyJsonWithoutUnivAndDeptAndStuffSerializationTest()
      throws IOException, ParseException {
    HistoryDto history = historyGetter.randomSelect();

    JSONObject json = makeJsonObject(
        HistoryResponse.from(history).withoutUniversityAndDepartment().withoutItem());
    historyJsonWithoutUnivAndDeptAndItemInfoCmpAssertions(json, history);
  }
}