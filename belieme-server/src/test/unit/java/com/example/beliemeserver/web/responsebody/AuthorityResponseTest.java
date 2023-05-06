package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.util.RandomGetter;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;


public class AuthorityResponseTest extends BaseResponseTest {
    private final RandomGetter<DepartmentDto> deptGetter = new RandomGetter<>(stub.ALL_DEPTS);
    private final RandomGetter<Permission> permissionGetter = new RandomGetter<>(stub.ALL_PERMISSIONS);

    @RepeatedTest(10)
    @DisplayName("[-]_[`authority json serialization` 테스트]_[-]")
    public void departmentJsonSerializationTest() throws IOException, ParseException {
        AuthorityDto auth = new AuthorityDto(deptGetter.randomSelect(), permissionGetter.randomSelect());

        JSONObject json = makeJsonObject(AuthorityResponse.from(auth));

        authorityJsonCmpAssertions(json, auth);
    }
}