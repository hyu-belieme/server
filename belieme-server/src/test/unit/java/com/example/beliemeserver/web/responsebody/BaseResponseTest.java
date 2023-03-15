package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto.*;
import com.example.beliemeserver.util.StubData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BaseResponseTest {
    protected StubData stub = new StubData();

    protected JSONObject makeJsonObject(Object object) throws JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(object);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(jsonString);
    }

    protected void basicUnivJsonCmpAssertions(JSONObject json, UniversityDto university) {
        System.out.println("DTO : " + university);
        System.out.println("JSON : " + json.toString());
        System.out.println();

        Assertions.assertThat(json.containsKey("code")).isTrue();
        Assertions.assertThat(json.get("code")).isEqualTo(university.code());

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

        Assertions.assertThat(json.containsKey("authorities")).isTrue();
        JSONArray jsonArray = (JSONArray) json.get("authorities");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject authorityJson = (JSONObject) jsonArray.get(i);
            authorityJsonCmpAssertions(authorityJson, user.meaningfulAuthorities().get(i));
        }

        Assertions.assertThat(json.containsKey("token")).isTrue();
        Assertions.assertThat(json.get("token")).isEqualTo(user.token());

        Assertions.assertThat(json.containsKey("createTimeStamp")).isTrue();
        Assertions.assertThat(json.get("createTimeStamp")).isEqualTo(user.createTimeStamp());

        Assertions.assertThat(json.containsKey("approvalTimeStamp")).isTrue();
        Assertions.assertThat(json.get("approvalTimeStamp")).isEqualTo(user.approvalTimeStamp());
    }

    protected void userWithoutSecureInfoJsonCmpAssertions(JSONObject json, UserDto user) {
        System.out.println("DTO : " + user);
        System.out.println("JSON : " + json.toString());
        System.out.println();

        userInfoJsonCmpAssertions(json, user);

        Assertions.assertThat(json.containsKey("authorities")).isFalse();
        Assertions.assertThat(json.containsKey("token")).isFalse();
        Assertions.assertThat(json.containsKey("createTimeStamp")).isFalse();
        Assertions.assertThat(json.containsKey("approveTimeStamp")).isFalse();
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

        Assertions.assertThat(json.containsKey("name")).isTrue();
        Assertions.assertThat(json.get("name")).isEqualTo(stuff.name());

        Assertions.assertThat(json.containsKey("emoji")).isTrue();
        Assertions.assertThat(json.get("emoji")).isEqualTo(stuff.emoji());

        Assertions.assertThat(json.containsKey("amount")).isTrue();
        Assertions.assertThat(json.get("amount")).isEqualTo((long) stuff.amount());

        Assertions.assertThat(json.containsKey("count")).isTrue();
        Assertions.assertThat(json.get("count")).isEqualTo((long) stuff.count());

        Assertions.assertThat(json.containsKey("itemList")).isTrue();
        JSONArray jsonArray = (JSONArray) json.get("itemList");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject itemJson = (JSONObject) jsonArray.get(i);
            itemJsonWithoutUnivAndDeptAndStuffInfoCmpAssertions(itemJson, stuff.items().get(i));
        }
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

        Assertions.assertThat(json.containsKey("stuffName")).isTrue();
        Assertions.assertThat(json.get("stuffName")).isEqualTo(item.stuff().name());

        Assertions.assertThat(json.containsKey("stuffEmoji")).isTrue();
        Assertions.assertThat(json.get("stuffEmoji")).isEqualTo(item.stuff().emoji());

        itemInfoJsonCmpAssertions(json, item);

        historyJsonNestedToItemCmpAssertions(json, item);
    }

    protected void itemJsonWithoutUnivAndDeptInfoCmpAssertions(JSONObject json, ItemDto item) {
        System.out.println("DTO : " + item);
        System.out.println("JSON : " + json.toString());
        System.out.println();

        Assertions.assertThat(json.containsKey("university")).isFalse();
        Assertions.assertThat(json.containsKey("department")).isFalse();

        Assertions.assertThat(json.containsKey("stuffName")).isTrue();
        Assertions.assertThat(json.get("stuffName")).isEqualTo(item.stuff().name());

        Assertions.assertThat(json.containsKey("stuffEmoji")).isTrue();
        Assertions.assertThat(json.get("stuffEmoji")).isEqualTo(item.stuff().emoji());

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

        Assertions.assertThat(json.containsKey("stuffName")).isFalse();
        Assertions.assertThat(json.containsKey("stuffEmoji")).isFalse();

        itemInfoJsonCmpAssertions(json, item);

        historyJsonNestedToItemCmpAssertions(json, item);
    }

    protected void itemJsonWithoutUnivAndDeptAndStuffInfoCmpAssertions(JSONObject json, ItemDto item) {
        System.out.println("DTO : " + item);
        System.out.println("JSON : " + json.toString());
        System.out.println();

        Assertions.assertThat(json.containsKey("university")).isFalse();
        Assertions.assertThat(json.containsKey("department")).isFalse();
        Assertions.assertThat(json.containsKey("stuffName")).isFalse();
        Assertions.assertThat(json.containsKey("stuffEmoji")).isFalse();

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

        Assertions.assertThat(json.containsKey("item")).isTrue();
        JSONObject itemJson = (JSONObject) json.get("item");
        itemJsonWithoutUnivAndDeptInfoCmpAssertions(itemJson, history.item());

        historyInfoJsonCmpAssertions(json, history);
    }

    protected void historyJsonWithoutUnivAndDeptInfoCmpAssertions(JSONObject json, HistoryDto history) {
        System.out.println("DTO : " + history);
        System.out.println("JSON : " + json.toString());
        System.out.println();

        Assertions.assertThat(json.containsKey("university")).isFalse();
        Assertions.assertThat(json.containsKey("department")).isFalse();

        Assertions.assertThat(json.containsKey("item")).isTrue();
        JSONObject itemJson = (JSONObject) json.get("item");
        itemJsonWithoutUnivAndDeptInfoCmpAssertions(itemJson, history.item());

        historyInfoJsonCmpAssertions(json, history);
    }

    protected void historyJsonWithoutItemInfoCmpAssertions(JSONObject json, HistoryDto history) {
        System.out.println("DTO : " + history);
        System.out.println("JSON : " + json.toString());
        System.out.println();

        Assertions.assertThat(json.containsKey("university")).isTrue();
        JSONObject univJson = (JSONObject) json.get("university");
        basicUnivJsonCmpAssertions(univJson, history.item().stuff().department().university());

        Assertions.assertThat(json.containsKey("department")).isTrue();
        JSONObject deptJson = (JSONObject) json.get("department");
        deptWithoutUnivJsonCmpAssertions(deptJson, history.item().stuff().department());

        Assertions.assertThat(json.containsKey("item")).isFalse();

        historyInfoJsonCmpAssertions(json, history);
    }

    protected void historyJsonWithoutUnivAndDeptAndItemInfoCmpAssertions(JSONObject json, HistoryDto history) {
        System.out.println("DTO : " + history);
        System.out.println("JSON : " + json.toString());
        System.out.println();

        Assertions.assertThat(json.containsKey("university")).isFalse();
        Assertions.assertThat(json.containsKey("department")).isFalse();
        Assertions.assertThat(json.containsKey("item")).isFalse();

        historyInfoJsonCmpAssertions(json, history);
    }

    private void deptInfoJsonCmpAssertions(JSONObject json, DepartmentDto department) {
        Assertions.assertThat(json.containsKey("code")).isTrue();
        Assertions.assertThat(json.get("code")).isEqualTo(department.code());

        Assertions.assertThat(json.containsKey("name")).isTrue();
        Assertions.assertThat(json.get("name")).isEqualTo(department.name());

        Assertions.assertThat(json.containsKey("baseMajors")).isTrue();
        JSONArray jsonArray = (JSONArray) json.get("baseMajors");
        for (int i = 0; i < jsonArray.size(); i++) {
            Assertions.assertThat(jsonArray.get(i)).isEqualTo(department.baseMajors().get(i).code());
        }
    }

    private void userInfoJsonCmpAssertions(JSONObject json, UserDto user) {
        Assertions.assertThat(json.containsKey("university")).isTrue();
        JSONObject univJson = (JSONObject) json.get("university");
        basicUnivJsonCmpAssertions(univJson, user.university());

        Assertions.assertThat(json.containsKey("studentId")).isTrue();
        Assertions.assertThat(json.get("studentId")).isEqualTo(user.studentId());

        Assertions.assertThat(json.containsKey("name")).isTrue();
        Assertions.assertThat(json.get("name")).isEqualTo(user.name());
    }

    private void itemInfoJsonCmpAssertions(JSONObject json, ItemDto item) {
        Assertions.assertThat(json.containsKey("num")).isTrue();
        Assertions.assertThat(json.get("num")).isEqualTo((long) item.num());

        Assertions.assertThat(json.containsKey("status")).isTrue();
        Assertions.assertThat(json.get("status")).isEqualTo(item.status().toString());
    }

    private void historyInfoJsonCmpAssertions(JSONObject json, HistoryDto history) {
        Assertions.assertThat(json.containsKey("num")).isTrue();
        Assertions.assertThat(json.get("num")).isEqualTo((long) history.num());

        userJsonNestedToHistoryCmpAssertions("requester", json, history.requester());
        userJsonNestedToHistoryCmpAssertions("approveManager", json, history.approveManager());
        userJsonNestedToHistoryCmpAssertions("returnManager", json, history.returnManager());
        userJsonNestedToHistoryCmpAssertions("lostManager", json, history.lostManager());
        userJsonNestedToHistoryCmpAssertions("cancelManager", json, history.cancelManager());

        timestampOnHistoryJsonCmpAssertions("reservedTimeStamp", json, history.reservedTimeStamp());
        timestampOnHistoryJsonCmpAssertions("approveTimeStamp", json, history.approveTimeStamp());
        timestampOnHistoryJsonCmpAssertions("returnTimeStamp", json, history.returnTimeStamp());
        timestampOnHistoryJsonCmpAssertions("lostTimeStamp", json, history.lostTimeStamp());
        timestampOnHistoryJsonCmpAssertions("cancelTimeStamp", json, history.cancelTimeStamp());

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
        historyJsonWithoutUnivAndDeptAndItemInfoCmpAssertions(historyJson, item.lastHistory());
    }
}
