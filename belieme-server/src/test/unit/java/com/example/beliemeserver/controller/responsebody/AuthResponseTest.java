package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.util.RandomGetter;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;


public class AuthResponseTest extends BaseResponseTest {
    @RepeatedTest(10)
    @DisplayName("[-]_[`authority json serialization` 테스트]_[-]")
    public void departmentJsonSerializationTest() throws IOException, ParseException {
        RandomGetter<DepartmentDto> deptGetter = new RandomGetter<>(stub.ALL_DEPTS);
        RandomGetter<AuthorityDto.Permission> permissionGetter = new RandomGetter<>(stub.ALL_PERMISSIONS);
        AuthorityDto auth = new AuthorityDto(deptGetter.randomSelect(), permissionGetter.randomSelect());

        JSONObject json = makeJsonObject(AuthorityResponse.from(auth));

        authorityJsonCmpAssertions(json, auth);
    }
}