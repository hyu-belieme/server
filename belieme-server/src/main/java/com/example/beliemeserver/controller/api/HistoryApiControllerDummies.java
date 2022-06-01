package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.HistoryResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HistoryApiControllerDummies {
    DummyData dummyData = DummyData.dummyData;

    @GetMapping("/histories/")
    public List<HistoryResponse> getAllHistoriesDummies(@RequestHeader("user-token") String userToken, @RequestParam(name = "studentId", required = false) String studentId) {
        return dummyData.historyList;
    }

    @GetMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/")
    public HistoryResponse getHistoryDummies(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) {
        return dummyData.history;
    }

    @PostMapping("/stuffs/{stuffName}/items/{itemNum}/histories/reserve")
    public HistoryResponse postReserveHistoryDummies(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum) {
        return dummyData.requestedHistory;
    }

    @PostMapping("/stuffs/{stuffName}/items/{itemNum}/histories/lost")
    public HistoryResponse postLostHistoryDummies(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum) {
        return dummyData.lostHistory;
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/cancel")
    public HistoryResponse patchHistoryToCancelDummies(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) {
        return dummyData.expiredHistory;
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/approve")
    public HistoryResponse patchHistoryToApproveDummies(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) {
        return dummyData.usingHistory;
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/return")
    public HistoryResponse patchHistoryToReturnDummies(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) {
        return dummyData.returnedHistory;
    }
}
