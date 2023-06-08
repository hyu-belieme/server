package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.util.StubData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BaseResponseTest {

  protected StubData stub = new StubData();

  protected JSONObject makeJsonObject(Object object)
      throws JsonProcessingException, ParseException {
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = mapper.writeValueAsString(object);
    JSONParser parser = new JSONParser();
    return (JSONObject) parser.parse(jsonString);
  }

  protected void basicUnivJsonCmpAssertions(JSONObject json, UniversityDto university) {
    System.out.println("DTO : " + university);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("id")).isTrue();
    Assertions.assertThat(json.get("id")).isEqualTo(university.id().toString());

    Assertions.assertThat(json.containsKey("name")).isTrue();
    Assertions.assertThat(json.get("name")).isEqualTo(university.name());
  }

  protected void basicDeptJsonCmpAssertions(JSONObject json, DepartmentDto department) {
    System.out.println("DTO : " + department);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    deptInfoJsonCmpAssertions(json, department);

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, department.university());
  }

  protected void deptWithoutUnivJsonCmpAssertions(JSONObject json, DepartmentDto department) {
    System.out.println("DTO : " + department);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    deptInfoJsonCmpAssertions(json, department);
    Assertions.assertThat(json.containsKey("university")).isFalse();
  }

  protected void authorityJsonCmpAssertions(JSONObject json, AuthorityDto authority) {
    System.out.println("DTO : " + authority);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("department")).isTrue();
    JSONObject deptJson = (JSONObject) json.get("department");
    basicDeptJsonCmpAssertions(deptJson, authority.department());

    Assertions.assertThat(json.containsKey("permission")).isTrue();
    Assertions.assertThat(json.get("permission")).isEqualTo(authority.permission().toString());
  }

  protected void userJsonCmpAssertions(JSONObject json, UserDto user) {
    System.out.println("DTO : " + user);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    userInfoJsonCmpAssertions(json, user);

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, user.university());

    Assertions.assertThat(json.containsKey("authorities")).isTrue();
    JSONArray jsonArray = (JSONArray) json.get("authorities");
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject authorityJson = (JSONObject) jsonArray.get(i);
      authorityJsonCmpAssertions(authorityJson, user.meaningfulAuthorities().get(i));
    }

    Assertions.assertThat(json.containsKey("token")).isTrue();
    Assertions.assertThat(json.get("token")).isEqualTo(user.token());

    Assertions.assertThat(json.containsKey("createdAt")).isTrue();
    Assertions.assertThat(json.get("createdAt")).isEqualTo(user.createdAt());

    Assertions.assertThat(json.containsKey("approvedAt")).isTrue();
    Assertions.assertThat(json.get("approvedAt")).isEqualTo(user.approvedAt());
  }

  protected void userWithoutSecureInfoJsonCmpAssertions(JSONObject json, UserDto user) {
    System.out.println("DTO : " + user);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    userInfoJsonCmpAssertions(json, user);

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, user.university());

    Assertions.assertThat(json.containsKey("authorities")).isTrue();
    JSONArray jsonArray = (JSONArray) json.get("authorities");
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject authorityJson = (JSONObject) jsonArray.get(i);
      authorityJsonCmpAssertions(authorityJson, user.meaningfulAuthorities().get(i));
    }

    Assertions.assertThat(json.containsKey("token")).isFalse();
    Assertions.assertThat(json.containsKey("createdAt")).isFalse();
    Assertions.assertThat(json.containsKey("approvedAt")).isFalse();
  }

  protected void stuffJsonCmpAssertions(JSONObject json, StuffDto stuff) {
    System.out.println("DTO : " + stuff);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, stuff.department().university());

    Assertions.assertThat(json.containsKey("department")).isTrue();
    JSONObject deptJson = (JSONObject) json.get("department");
    deptWithoutUnivJsonCmpAssertions(deptJson, stuff.department());

    Assertions.assertThat(json.containsKey("items")).isTrue();
    JSONArray jsonArray = (JSONArray) json.get("items");
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject itemJson = (JSONObject) jsonArray.get(i);
      itemJsonWithoutUnivAndDeptAndStuffInfoCmpAssertions(itemJson, stuff.items().get(i));
    }

    stuffInfoJsonCmpAssertions(json, stuff);
  }

  protected void stuffJsonWithoutUnivAndDeptInfoCmpAssertions(JSONObject json, StuffDto stuff) {
    System.out.println("DTO : " + stuff);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isFalse();

    Assertions.assertThat(json.containsKey("department")).isFalse();

    Assertions.assertThat(json.containsKey("items")).isTrue();
    JSONArray jsonArray = (JSONArray) json.get("items");
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject itemJson = (JSONObject) jsonArray.get(i);
      itemJsonWithoutUnivAndDeptAndStuffInfoCmpAssertions(itemJson, stuff.items().get(i));
    }

    stuffInfoJsonCmpAssertions(json, stuff);
  }

  protected void stuffJsonWithoutItemsCmpAssertions(JSONObject json, StuffDto stuff) {
    System.out.println("DTO : " + stuff);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, stuff.department().university());

    Assertions.assertThat(json.containsKey("department")).isTrue();
    JSONObject deptJson = (JSONObject) json.get("department");
    deptWithoutUnivJsonCmpAssertions(deptJson, stuff.department());

    Assertions.assertThat(json.containsKey("items")).isFalse();

    stuffInfoJsonCmpAssertions(json, stuff);
  }

  protected void stuffJsonWithoutUnivAndDeptAndItemsInfoCmpAssertions(JSONObject json, StuffDto stuff) {
    System.out.println("DTO : " + stuff);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isFalse();

    Assertions.assertThat(json.containsKey("department")).isFalse();

    Assertions.assertThat(json.containsKey("items")).isFalse();

    stuffInfoJsonCmpAssertions(json, stuff);
  }


  protected void itemJsonCmpAssertions(JSONObject json, ItemDto item) {
    System.out.println("DTO : " + item);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, item.stuff().department().university());

    Assertions.assertThat(json.containsKey("department")).isTrue();
    JSONObject deptJson = (JSONObject) json.get("department");
    deptWithoutUnivJsonCmpAssertions(deptJson, item.stuff().department());

    Assertions.assertThat(json.containsKey("stuff")).isTrue();
    JSONObject stuffJson = (JSONObject) json.get("stuff");
    stuffInfoJsonCmpAssertions(stuffJson, item.stuff());

    itemInfoJsonCmpAssertions(json, item);

    historyJsonNestedToItemCmpAssertions(json, item);
  }

  protected void itemJsonWithoutUnivAndDeptInfoCmpAssertions(JSONObject json, ItemDto item) {
    System.out.println("DTO : " + item);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isFalse();
    Assertions.assertThat(json.containsKey("department")).isFalse();

    Assertions.assertThat(json.containsKey("stuff")).isTrue();
    JSONObject stuffJson = (JSONObject) json.get("stuff");
    stuffInfoJsonCmpAssertions(stuffJson, item.stuff());

    itemInfoJsonCmpAssertions(json, item);

    historyJsonNestedToItemCmpAssertions(json, item);
  }

  protected void itemJsonWithoutStuffInfoCmpAssertions(JSONObject json, ItemDto item) {
    System.out.println("DTO : " + item);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, item.stuff().department().university());

    Assertions.assertThat(json.containsKey("department")).isTrue();
    JSONObject deptJson = (JSONObject) json.get("department");
    deptWithoutUnivJsonCmpAssertions(deptJson, item.stuff().department());

    Assertions.assertThat(json.containsKey("stuff")).isFalse();

    itemInfoJsonCmpAssertions(json, item);

    historyJsonNestedToItemCmpAssertions(json, item);
  }

  protected void itemJsonWithoutUnivAndDeptAndStuffInfoCmpAssertions(JSONObject json,
      ItemDto item) {
    System.out.println("DTO : " + item);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isFalse();
    Assertions.assertThat(json.containsKey("department")).isFalse();
    Assertions.assertThat(json.containsKey("stuff")).isFalse();

    itemInfoJsonCmpAssertions(json, item);

    historyJsonNestedToItemCmpAssertions(json, item);
  }

  protected void historyJsoCmpAssertions(JSONObject json, HistoryDto history) {
    System.out.println("DTO : " + history);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, history.item().stuff().department().university());

    Assertions.assertThat(json.containsKey("department")).isTrue();
    JSONObject deptJson = (JSONObject) json.get("department");
    deptWithoutUnivJsonCmpAssertions(deptJson, history.item().stuff().department());

    Assertions.assertThat(json.containsKey("stuff")).isTrue();
    JSONObject stuffJson = (JSONObject) json.get("stuff");
    stuffJsonWithoutUnivAndDeptAndItemsInfoCmpAssertions(stuffJson, history.item().stuff());

    Assertions.assertThat(json.containsKey("item")).isTrue();
    JSONObject itemJson = (JSONObject) json.get("item");
    itemJsonWithoutUnivAndDeptAndStuffInfoCmpAssertions(itemJson, history.item());

    historyInfoJsonCmpAssertions(json, history);
  }

  protected void historyJsonWithoutUnivAndDeptInfoCmpAssertions(JSONObject json,
      HistoryDto history) {
    System.out.println("DTO : " + history);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isFalse();
    Assertions.assertThat(json.containsKey("department")).isFalse();

    Assertions.assertThat(json.containsKey("stuff")).isTrue();
    JSONObject stuffJson = (JSONObject) json.get("stuff");
    stuffJsonWithoutUnivAndDeptAndItemsInfoCmpAssertions(stuffJson, history.item().stuff());

    Assertions.assertThat(json.containsKey("item")).isTrue();
    JSONObject itemJson = (JSONObject) json.get("item");
    itemJsonWithoutUnivAndDeptAndStuffInfoCmpAssertions(itemJson, history.item());

    historyInfoJsonCmpAssertions(json, history);
  }

  protected void historyJsonWithoutStuffAndItemInfoCmpAssertions(JSONObject json, HistoryDto history) {
    System.out.println("DTO : " + history);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isTrue();
    JSONObject univJson = (JSONObject) json.get("university");
    basicUnivJsonCmpAssertions(univJson, history.item().stuff().department().university());

    Assertions.assertThat(json.containsKey("department")).isTrue();
    JSONObject deptJson = (JSONObject) json.get("department");
    deptWithoutUnivJsonCmpAssertions(deptJson, history.item().stuff().department());

    Assertions.assertThat(json.containsKey("stuff")).isFalse();
    Assertions.assertThat(json.containsKey("item")).isFalse();

    historyInfoJsonCmpAssertions(json, history);
  }

  protected void historyJsonWithoutUnivAndDeptAndStuffAndItemInfoCmpAssertions(JSONObject json,
      HistoryDto history) {
    System.out.println("DTO : " + history);
    System.out.println("JSON : " + json.toString());
    System.out.println();

    Assertions.assertThat(json.containsKey("university")).isFalse();
    Assertions.assertThat(json.containsKey("department")).isFalse();
    Assertions.assertThat(json.containsKey("stuff")).isFalse();
    Assertions.assertThat(json.containsKey("item")).isFalse();

    historyInfoJsonCmpAssertions(json, history);
  }

  private void deptInfoJsonCmpAssertions(JSONObject json, DepartmentDto department) {
    Assertions.assertThat(json.containsKey("id")).isTrue();
    Assertions.assertThat(json.get("id")).isEqualTo(department.id().toString());

    Assertions.assertThat(json.containsKey("name")).isTrue();
    Assertions.assertThat(json.get("name")).isEqualTo(department.name());

    Assertions.assertThat(json.containsKey("baseMajors")).isTrue();
    JSONArray jsonArray = (JSONArray) json.get("baseMajors");
    for (int i = 0; i < jsonArray.size(); i++) {
      Assertions.assertThat(jsonArray.get(i)).isEqualTo(department.baseMajors().get(i).code());
    }
  }

  private void userInfoJsonCmpAssertions(JSONObject json, UserDto user) {
    Assertions.assertThat(json.containsKey("id")).isTrue();
    Assertions.assertThat(json.get("id")).isEqualTo(user.id().toString());

    Assertions.assertThat(json.containsKey("studentId")).isTrue();
    Assertions.assertThat(json.get("studentId")).isEqualTo(user.studentId());

    Assertions.assertThat(json.containsKey("name")).isTrue();
    Assertions.assertThat(json.get("name")).isEqualTo(user.name());

    if (user.entranceYear() == 0) {
      Assertions.assertThat(json.containsKey("entranceYear")).isFalse();
      return;
    }
    Assertions.assertThat(json.containsKey("entranceYear")).isTrue();
    Assertions.assertThat(json.get("entranceYear")).isEqualTo((long) user.entranceYear());
  }

  private void stuffInfoJsonCmpAssertions(JSONObject json, StuffDto stuff) {
    Assertions.assertThat(json.containsKey("id")).isTrue();
    Assertions.assertThat(json.get("id")).isEqualTo(stuff.id().toString());

    Assertions.assertThat(json.containsKey("name")).isTrue();
    Assertions.assertThat(json.get("name")).isEqualTo(stuff.name());

    Assertions.assertThat(json.containsKey("thumbnail")).isTrue();
    Assertions.assertThat(json.get("thumbnail")).isEqualTo(stuff.thumbnail());

    Assertions.assertThat(json.containsKey("amount")).isTrue();
    Assertions.assertThat(json.get("amount")).isEqualTo((long) stuff.amount());

    Assertions.assertThat(json.containsKey("count")).isTrue();
    Assertions.assertThat(json.get("count")).isEqualTo((long) stuff.count());
  }

  private void itemInfoJsonCmpAssertions(JSONObject json, ItemDto item) {
    Assertions.assertThat(json.containsKey("id")).isTrue();
    Assertions.assertThat(json.get("id")).isEqualTo(item.id().toString());

    Assertions.assertThat(json.containsKey("num")).isTrue();
    Assertions.assertThat(json.get("num")).isEqualTo((long) item.num());

    Assertions.assertThat(json.containsKey("status")).isTrue();
    Assertions.assertThat(json.get("status")).isEqualTo(item.status().toString());
  }

  private void historyInfoJsonCmpAssertions(JSONObject json, HistoryDto history) {
    Assertions.assertThat(json.containsKey("id")).isTrue();
    Assertions.assertThat(json.get("id")).isEqualTo(history.id().toString());

    Assertions.assertThat(json.containsKey("num")).isTrue();
    Assertions.assertThat(json.get("num")).isEqualTo((long) history.num());

    userJsonNestedToHistoryCmpAssertions("requester", json, history.requester());
    userJsonNestedToHistoryCmpAssertions("approveManager", json, history.approveManager());
    userJsonNestedToHistoryCmpAssertions("returnManager", json, history.returnManager());
    userJsonNestedToHistoryCmpAssertions("lostManager", json, history.lostManager());
    userJsonNestedToHistoryCmpAssertions("cancelManager", json, history.cancelManager());

    timestampOnHistoryJsonCmpAssertions("requestedAt", json, history.requestedAt());
    timestampOnHistoryJsonCmpAssertions("approvedAt", json, history.approvedAt());
    timestampOnHistoryJsonCmpAssertions("returnedAt", json, history.returnedAt());
    timestampOnHistoryJsonCmpAssertions("lostAt", json, history.lostAt());
    timestampOnHistoryJsonCmpAssertions("canceledAt", json, history.canceledAt());

    Assertions.assertThat(json.containsKey("status")).isTrue();
    Assertions.assertThat(json.get("status")).isEqualTo(history.status().toString());
  }

  private void timestampOnHistoryJsonCmpAssertions(String tag, JSONObject json, long timeStamp) {
    if (timeStamp == 0) {
      Assertions.assertThat(json.containsKey(tag)).isFalse();
      return;
    }

    Assertions.assertThat(json.containsKey(tag)).isTrue();
    Assertions.assertThat(json.get(tag)).isEqualTo(timeStamp);
  }

  private void userJsonNestedToHistoryCmpAssertions(String tag, JSONObject json, UserDto user) {
    if (user != null) {
      Assertions.assertThat(json.containsKey(tag)).isTrue();
      JSONObject userJson = (JSONObject) json.get(tag);
      userWithoutSecureInfoJsonCmpAssertions(userJson, user);
    }
  }

  private void historyJsonNestedToItemCmpAssertions(JSONObject json, ItemDto item) {
    Assertions.assertThat(json.containsKey("lastHistory")).isTrue();
    JSONObject historyJson = (JSONObject) json.get("lastHistory");
    if (item.lastHistory() == null) {
      Assertions.assertThat(historyJson).isNull();
      return;
    }
    historyJsonWithoutUnivAndDeptAndStuffAndItemInfoCmpAssertions(historyJson, item.lastHistory());
  }
}
