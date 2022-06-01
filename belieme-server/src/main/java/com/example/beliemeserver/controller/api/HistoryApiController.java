package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.ItemRequest;
import com.example.beliemeserver.controller.responsebody.HistoryResponse;
import com.example.beliemeserver.controller.util.Globals;

import com.example.beliemeserver.model.exception.*;
import com.example.beliemeserver.model.service.HistoryService;
import com.example.beliemeserver.model.dto.HistoryDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HistoryApiController {
    private final HistoryService historyService;

    public HistoryApiController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/real/histories/")
    public ResponseEntity<List<HistoryResponse>> getAllHistories(@RequestHeader("user-token") String userToken, @RequestParam(name = "studentId", required = false) String studentId) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException {
        List<HistoryDto> historyList;
        try {
            historyList = historyService.getHistories(userToken, studentId);
        } catch (DataException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        }
        return ResponseEntity.ok(toHistoryResponse(historyList));
    }

    @GetMapping("/real/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/")
    public ResponseEntity<HistoryResponse> getHistory(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws InternalServerErrorHttpException, UnauthorizedHttpException, NotFoundHttpException, ForbiddenHttpException {
        HistoryDto target;

        try {
            target = historyService.getHistoryByStuffNameAndItemNumAndHistoryNum(userToken, stuffName, itemNum, historyNum);
        } catch (DataException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        }

        return ResponseEntity.ok(HistoryResponse.from(target));
    }

    @PostMapping("/real/histories/reserve")
    public ResponseEntity<HistoryResponse> postReserveHistory(@RequestHeader("user-token") String userToken, @RequestBody ItemRequest itemRequest) throws BadRequestHttpException, InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, ConflictHttpException {
        if(itemRequest == null) {
            throw new BadRequestHttpException("'stuff_name'은 필수입니다.('item_num'은 Optional 입니다)");
        }

        HistoryDto savedHistory = null;
        try {
            savedHistory = historyService.addReserveHistory(userToken, itemRequest.getStuffName(), itemRequest.getItemNum());
        } catch (DataException | ItCannotBeException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        } catch (ConflictException e) {
            e.printStackTrace();
            throw new ConflictHttpException(e);
        }

        URI location = Globals.getLocation(Globals.serverUrl + "/stuffs/" + savedHistory.getItem().getStuff().getName() + "/items/" + savedHistory.getItem().getNum() + "/histories/" + savedHistory.getNum());
        return ResponseEntity.created(location).body(HistoryResponse.from(savedHistory));
    }

    @PatchMapping("/real/stuffs/{stuffName}/items/{itemNum}/histories/lost")
    public ResponseEntity<HistoryResponse> postLostHistory(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum) throws InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException, NotFoundHttpException, MethodNotAllowedHttpException, ConflictHttpException {
        HistoryDto savedHistoryDto;
        try {
            savedHistoryDto = historyService.addOrEditToLostHistory(userToken, stuffName, itemNum);
        } catch (DataException | ItCannotBeException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        } catch (ConflictException e) {
            e.printStackTrace();
            throw new ConflictHttpException(e);
        }
        return ResponseEntity.ok(HistoryResponse.from(savedHistoryDto));
    }

    @PatchMapping("/real/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/cancel")
    public ResponseEntity<HistoryResponse> patchHistoryToCancel(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, NotFoundHttpException {
        HistoryDto updatedHistory;
        try {
            updatedHistory = historyService.editToCanceledHistory(userToken, stuffName, itemNum, historyNum);
        } catch (DataException | ItCannotBeException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        }
        return ResponseEntity.ok(HistoryResponse.from(updatedHistory));
    }

    @PatchMapping("/real/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/approve")
    public ResponseEntity<HistoryResponse> patchHistoryToApprove(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, NotFoundHttpException {
        HistoryDto updatedHistory;
        try {
            updatedHistory = historyService.editToApprovedHistory(userToken, stuffName, itemNum, historyNum);
        } catch (DataException | ItCannotBeException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        }
        return ResponseEntity.ok(HistoryResponse.from(updatedHistory));
    }

    @PatchMapping("/real/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/return")
    public ResponseEntity<HistoryResponse> patchHistoryToReturn(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, NotFoundHttpException {
        HistoryDto updatedHistory;
        try {
            updatedHistory = historyService.editToReturnedHistory(userToken, stuffName, itemNum, historyNum);
        } catch (DataException | ItCannotBeException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        }
        return ResponseEntity.ok(HistoryResponse.from(updatedHistory));
    }

    @PatchMapping("/real/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/found")
    public ResponseEntity<HistoryResponse> patchHistoryToFound(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, NotFoundHttpException {
        HistoryDto updatedHistory;
        try {
            updatedHistory = historyService.editToFoundHistory(userToken, stuffName, itemNum, historyNum);
        } catch (DataException | ItCannotBeException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        }
        return ResponseEntity.ok(HistoryResponse.from(updatedHistory));
    }

    private List<HistoryResponse> toHistoryResponse(List<HistoryDto> historyDtoList) {
        List<HistoryResponse> historyResponseList = new ArrayList<>();
        for(int i = 0; i < historyDtoList.size(); i++) {
            historyResponseList.add(HistoryResponse.from(historyDtoList.get(i)));
        }
        return historyResponseList;
    }
}
