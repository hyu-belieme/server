package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.*;
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
        System.out.println(json.toString());

        Assertions.assertThat(json.containsKey("code")).isTrue();
        Assertions.assertThat(json.get("code")).isEqualTo(university.code());

        Assertions.assertThat(json.containsKey("name")).isTrue();
        Assertions.assertThat(json.get("name")).isEqualTo(university.name());
    }

    protected void basicDeptJsonCmpAssertions(JSONObject json, DepartmentDto department) {
        System.out.println(json.toString());

        deptInfoJsonCmpAssertions(json, department);

        Assertions.assertThat(json.containsKey("university")).isTrue();
        JSONObject univJson = (JSONObject) json.get("university");
        basicUnivJsonCmpAssertions(univJson, department.university());
    }

    protected void deptWithoutUnivJsonCmpAssertions(JSONObject json, DepartmentDto department) {
        System.out.println(json.toString());

        deptInfoJsonCmpAssertions(json, department);
        Assertions.assertThat(json.containsKey("university")).isFalse();
    }

    protected void authorityJsonCmpAssertions(JSONObject json, AuthorityDto authority) {
        System.out.println(json.toString());

        Assertions.assertThat(json.containsKey("department")).isTrue();
        JSONObject deptJson = (JSONObject) json.get("department");
        basicDeptJsonCmpAssertions(deptJson, authority.department());

        Assertions.assertThat(json.containsKey("permission")).isTrue();
        Assertions.assertThat(json.get("permission")).isEqualTo(authority.permission().toString());
    }

    protected void userJsonCmpAssertions(JSONObject json, UserDto user) {
        System.out.println(json.toString());

        userInfoJsonCmpAssertions(json, user);

        Assertions.assertThat(json.containsKey("token")).isTrue();
        Assertions.assertThat(json.get("token")).isEqualTo(user.token());

        Assertions.assertThat(json.containsKey("createTimeStamp")).isTrue();
        Assertions.assertThat(json.get("createTimeStamp")).isEqualTo(user.createTimeStamp());

        Assertions.assertThat(json.containsKey("approvalTimeStamp")).isTrue();
        Assertions.assertThat(json.get("approvalTimeStamp")).isEqualTo(user.approvalTimeStamp());
    }

    protected void userWithoutSecureInfoJsonCmpAssertions(JSONObject json, UserDto user) {
        System.out.println(json.toString());

        userInfoJsonCmpAssertions(json, user);

        Assertions.assertThat(json.containsKey("token")).isFalse();
        Assertions.assertThat(json.containsKey("createTimeStamp")).isFalse();
        Assertions.assertThat(json.containsKey("approvalTimeStamp")).isFalse();
    }

    protected void stuffJsonCmpAssertions(JSONObject json, StuffDto stuff) {
        System.out.println(json.toString());
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

        // TODO Item 해야함
    }

    private void deptInfoJsonCmpAssertions(JSONObject json, DepartmentDto department) {
        Assertions.assertThat(json.containsKey("code")).isTrue();
        Assertions.assertThat(json.get("code")).isEqualTo(department.code());

        Assertions.assertThat(json.containsKey("name")).isTrue();
        Assertions.assertThat(json.get("name")).isEqualTo(department.name());

        Assertions.assertThat(json.containsKey("baseMajors")).isTrue();
        JSONArray jsonArray = (JSONArray) json.get("baseMajors");
        for(int i = 0; i < jsonArray.size(); i++) {
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
}
