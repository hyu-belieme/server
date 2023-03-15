package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto.StuffDto;
import com.example.beliemeserver.util.RandomGetter;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;


public class StuffResponseTest extends BaseResponseTest {
    private final RandomGetter<StuffDto> stuffGetter = new RandomGetter<>(stub.ALL_STUFFS);

    @RepeatedTest(10)
    @DisplayName("[-]_[`stuff json serialization` 테스트]_[-]")
    public void stuffJsonSerializationTest() throws IOException, ParseException {
        StuffDto stuff = stuffGetter.randomSelect();

        JSONObject json = makeJsonObject(StuffResponse.from(stuff));
        stuffJsonCmpAssertions(json, stuff);
    }
}