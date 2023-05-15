package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.util.RandomGetter;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;


public class ItemResponseTest extends BaseResponseTest {
    private final RandomGetter<ItemDto> itemGetter = new RandomGetter<>(stub.ALL_ITEMS);

    @RepeatedTest(10)
    @DisplayName("[-]_[`item json serialization` 테스트]_[-]")
    public void itemJsonSerializationTest() throws IOException, ParseException {
        ItemDto item = itemGetter.randomSelect();

        JSONObject json = makeJsonObject(ItemResponse.from(item));
        itemJsonCmpAssertions(json, item);
    }

    @RepeatedTest(10)
    @DisplayName("[-]_[`university`와 `department`를 제외한 `item json serialization` 테스트]_[-]")
    public void itemJsonWithoutUnivAndDeptSerializationTest() throws IOException, ParseException {
        ItemDto item = itemGetter.randomSelect();

        JSONObject json = makeJsonObject(
                ItemResponse.from(item).withoutUniversityAndDepartment());
        itemJsonWithoutUnivAndDeptInfoCmpAssertions(json, item);
    }

    @RepeatedTest(10)
    @DisplayName("[-]_[`stuff` 관련 정보를 제외한 `item json serialization` 테스트]_[-]")
    public void itemJsonWithoutStuffSerializationTest() throws IOException, ParseException {
        ItemDto item = itemGetter.randomSelect();

        JSONObject json = makeJsonObject(
                ItemResponse.from(item).withoutStuffInfo());
        itemJsonWithoutStuffInfoCmpAssertions(json, item);
    }

    @RepeatedTest(10)
    @DisplayName("[-]_[`university`와 `department`, `stuff` 관련 정보를 제외한 `item json serialization` 테스트]_[-]")
    public void itemJsonWithoutUnivAndDeptAndStuffSerializationTest() throws IOException, ParseException {
        ItemDto item = itemGetter.randomSelect();

        JSONObject json = makeJsonObject(ItemResponse.from(item)
                .withoutUniversityAndDepartment()
                .withoutStuffInfo());
        itemJsonWithoutUnivAndDeptAndStuffInfoCmpAssertions(json, item);
    }
}