package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.util.RandomGetter;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;


public class UniversityResponseTest extends BaseResponseTest {
    private final RandomGetter<UniversityDto> univGetter = new RandomGetter<>(stub.ALL_UNIVS);

    @RepeatedTest(10)
    @DisplayName("[-]_[`university json serialization` 테스트]_[-]")
    public void universityJsonSerializationTest() throws IOException, ParseException {
        UniversityDto univ = univGetter.randomSelect();

        JSONObject json = makeJsonObject(UniversityResponse.from(univ));

        basicUnivJsonCmpAssertions(json, univ);
    }
}