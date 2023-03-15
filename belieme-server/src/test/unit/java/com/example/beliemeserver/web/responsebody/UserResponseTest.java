package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.util.RandomGetter;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;


public class UserResponseTest extends BaseResponseTest {
    private final RandomGetter<UserDto> userGetter = new RandomGetter<>(stub.ALL_USERS);

    @RepeatedTest(10)
    @DisplayName("[-]_[`user json serialization` 테스트]_[-]")
    public void userJsonSerializationTest() throws IOException, ParseException {
        UserDto user = userGetter.randomSelect();

        JSONObject json = makeJsonObject(UserResponse.from(user));
        userJsonCmpAssertions(json, user);
    }

    @RepeatedTest(10)
    @DisplayName("[-]_[`user without secure info json serialization` 테스트]_[-]")
    public void userWithoutSecureInfoJsonSerializationTest() throws IOException, ParseException {
        UserDto user = userGetter.randomSelect();

        JSONObject json = makeJsonObject(UserResponse.from(user).withoutSecureInfo());
        userWithoutSecureInfoJsonCmpAssertions(json, user);
    }
}