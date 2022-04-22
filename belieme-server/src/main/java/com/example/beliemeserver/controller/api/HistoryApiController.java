package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.HistoryResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HistoryApiController {
    DummyData dummyData = DummyData.dummyData;

    @GetMapping("/histories/")
    public List<HistoryResponse> getAllHistories(@RequestHeader("user-token") String userToken, @RequestParam(name = "studentId", required = false) String studentId) {
        return dummyData.historyList;
    }

    @GetMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/")
    public HistoryResponse getHistory(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) {
        return dummyData.history;
    }

    @PostMapping("/stuffs/{stuffName}/items/{itemNum}/histories/reserve")
    public HistoryResponse postReserveHistory(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum) {
        return dummyData.requestedHistory;
    }

    @PostMapping("/stuffs/{stuffName}/items/{itemNum}/histories/lost")
    public HistoryResponse postLostHistory(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum) {
        return dummyData.lostHistory;
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/cancel")
    public HistoryResponse patchHistoryToCancel(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) {
        return dummyData.expiredHistory;
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/approve")
    public HistoryResponse patchHistoryToApprove(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) {
        return dummyData.usingHistory;
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/return")
    public HistoryResponse patchHistoryToReturn(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) {
        return dummyData.returnedHistory;
    }
}
