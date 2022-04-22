package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.HistoryResponse;
import com.example.beliemeserver.controller.responsebody.ItemResponse;
import com.example.beliemeserver.controller.responsebody.StuffResponse;
import com.example.beliemeserver.controller.responsebody.UserResponse;

import java.util.ArrayList;

public class DummyData {
    public static DummyData dummyData = new DummyData();

    ArrayList<StuffResponse> stuffList;
    ArrayList<ItemResponse> itemList;
    ArrayList<HistoryResponse> historyList;

    StuffResponse stuff;
    HistoryResponse history;

    UserResponse seokhwan = new UserResponse("2018008886", "이석환", "TOKEN", 1650623521, 1650623521, "MASTER");
    UserResponse sangwon = new UserResponse("2018008881", "이상원", "TOKEN", 1650623521, 1650623521, "MASTER");
    UserResponse yelin = new UserResponse("2018008882", "김예린", "TOKEN", 1650623521, 1650623521, "MASTER");
    UserResponse kangwo = new UserResponse("2018008883", "박강우", "TOKEN", 1650623521, 1650623521, "USER");
    UserResponse taeuk = new UserResponse("2018008884", "김태욱", "TOKEN", 1650623521, 1650623521, "USER");

    HistoryResponse expiredHistory = new HistoryResponse(
            new ItemResponse("농구공", "🏀", 2, HistoryResponse.responseWillBeIgnore()),
            2,
            seokhwan.toUserResponseNestedInHistory(),
            yelin.toUserResponseNestedInHistory(),
            null,
            null,
            null,
            1650523521,
            0,
            0,
            0,
            0,
            "EXPIRED");

    HistoryResponse usingHistory = new HistoryResponse(
            new ItemResponse("우산", "🌂", 1, HistoryResponse.responseWillBeIgnore()),
            3,
            seokhwan.toUserResponseNestedInHistory(),
            yelin.toUserResponseNestedInHistory(),
            null,
            null,
            null,
            1650623521,
            1650624221,
            0,
            0,
            0,
            "USING");

    HistoryResponse lostHistory = new HistoryResponse(
            new ItemResponse("우산", "🌂", 2, HistoryResponse.responseWillBeIgnore()),
            3,
            seokhwan.toUserResponseNestedInHistory(),
            yelin.toUserResponseNestedInHistory(),
            null,
            taeuk.toUserResponseNestedInHistory(),
            null,
            1650623521,
            1650624221,
            0,
            1650631221,
            0,
            "LOST");

    HistoryResponse delayedHistory = new HistoryResponse(
            new ItemResponse("축구공", "⚽️", 1, HistoryResponse.responseWillBeIgnore()),
            1,
            taeuk.toUserResponseNestedInHistory(),
            seokhwan.toUserResponseNestedInHistory(),
            null,
            null,
            null,
            1650403521,
            1650404011,
            0,
            0,
            0,
            "DELAYED");

    HistoryResponse requestedHistory = new HistoryResponse(
            new ItemResponse("우산", "🌂", 4, HistoryResponse.responseWillBeIgnore()),
            3,
            sangwon.toUserResponseNestedInHistory(),
            null,
            null,
            null,
            null,
            1650623521,
            0,
            0,
            0,
            0,
            "REQUESTED");

    HistoryResponse returnedHistory = new HistoryResponse(
            new ItemResponse("우산", "🌂", 3, HistoryResponse.responseWillBeIgnore()),
            3,
            kangwo.toUserResponseNestedInHistory(),
            yelin.toUserResponseNestedInHistory(),
            yelin.toUserResponseNestedInHistory(),
            null,
            null,
            1650623521,
            1650624221,
            1650634221,
            0,
            0,
            "RETURNED");


    public DummyData() {
        setStuffList();
        setItemList();
        setHistoryList();
        stuff = new StuffResponse("우산", "🌂", 5, 3, itemList);
        history = new HistoryResponse(
                new ItemResponse("충전기", "🔌", 4, HistoryResponse.responseWillBeIgnore()),
                2,
                seokhwan.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                kangwo.toUserResponseNestedInHistory(),
                kangwo.toUserResponseNestedInHistory(),
                null,
                1650623521,
                1650624221,
                1650754221,
                1650724221,
                0,
                "USING");
    }

    void setStuffList() {
        stuffList = new ArrayList<>();
        stuffList.add(new StuffResponse("우산", "🌂", 5, 3, null));
        stuffList.add(new StuffResponse("블루투스 스피커", "📻", 2, 0, null));
        stuffList.add(new StuffResponse("축구공", "⚽️", 3, 3, null));
        stuffList.add(new StuffResponse("농구공", "🏀️", 2, 2, null));
        stuffList.add(new StuffResponse("충전기", "🔌", 10, 7, null));
    }

    void setHistoryList() {
        historyList = new ArrayList<>();
        historyList.add(new HistoryResponse(
                new ItemResponse("우산", "🌂", 1, HistoryResponse.responseWillBeIgnore()),
                3,
                seokhwan.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                null,
                null,
                null,
                1650623521,
                1650624221,
                0,
                0,
                0,
                "USING"));

        historyList.add(new HistoryResponse(
                new ItemResponse("우산", "🌂", 2, HistoryResponse.responseWillBeIgnore()),
                3,
                seokhwan.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                null,
                taeuk.toUserResponseNestedInHistory(),
                null,
                1650623521,
                1650624221,
                0,
                1650631221,
                0,
                "LOST"));

        historyList.add(new HistoryResponse(
                new ItemResponse("우산", "🌂", 3, HistoryResponse.responseWillBeIgnore()),
                3,
                kangwo.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                null,
                null,
                1650623521,
                1650624221,
                1650634221,
                0,
                0,
                "RETURNED"));

        historyList.add(new HistoryResponse(
                new ItemResponse("우산", "🌂", 4, HistoryResponse.responseWillBeIgnore()),
                3,
                sangwon.toUserResponseNestedInHistory(),
                null,
                null,
                null,
                null,
                1650623521,
                0,
                0,
                0,
                0,
                "REQUESTED"));

        historyList.add(new HistoryResponse(
                new ItemResponse("축구공", "⚽️", 1, HistoryResponse.responseWillBeIgnore()),
                1,
                taeuk.toUserResponseNestedInHistory(),
                seokhwan.toUserResponseNestedInHistory(),
                null,
                null,
                null,
                1650403521,
                1650404011,
                0,
                0,
                0,
                "DELAYED"));
    }

    void setItemList() {
        itemList = new ArrayList<>();

        itemList.add(new ItemResponse(null, null, 1, new HistoryResponse(
                null,
                3,
                seokhwan.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                null,
                null,
                null,
                1650623521,
                1650624221,
                0,
                0,
                0,
                "USING")));

        itemList.add(new ItemResponse(null, null, 2, new HistoryResponse(
                null,
                3,
                seokhwan.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                null,
                taeuk.toUserResponseNestedInHistory(),
                null,
                1650623521,
                1650624221,
                0,
                1650631221,
                0,
                "LOST")));

        itemList.add(new ItemResponse(null, null, 3, new HistoryResponse(
                null,
                3,
                kangwo.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                yelin.toUserResponseNestedInHistory(),
                null,
                null,
                1650623521,
                1650624221,
                1650634221,
                0,
                0,
                "RETURNED")));

        itemList.add(new ItemResponse(null, null, 4, new HistoryResponse(
                null,
                3,
                sangwon.toUserResponseNestedInHistory(),
                null,
                null,
                null,
                null,
                1650623521,
                0,
                0,
                0,
                0,
                "REQUESTED")));

        itemList.add(new ItemResponse(null, null, 5, null));
        itemList.add(new ItemResponse(null, null, 6, null));
    }
}
