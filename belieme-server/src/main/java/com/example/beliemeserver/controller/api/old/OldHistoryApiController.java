package com.example.beliemeserver.controller.api.old;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.old.OldItemRequest;
import com.example.beliemeserver.controller.responsebody.old.OldHistoryResponse;
import com.example.beliemeserver.common.Globals;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.model.service.old.OldHistoryService;
import com.example.beliemeserver.model.dto.old.OldHistoryDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/old")
public class OldHistoryApiController {
    private final OldHistoryService historyService;

    public OldHistoryApiController(OldHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/histories/")
    public ResponseEntity<List<OldHistoryResponse>> getAllHistories(@RequestHeader("user-token") String userToken, @RequestParam(name = "studentId", required = false) String studentId) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException {
        List<OldHistoryDto> historyList;
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

    @GetMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/")
    public ResponseEntity<OldHistoryResponse> getHistory(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws InternalServerErrorHttpException, UnauthorizedHttpException, NotFoundHttpException, ForbiddenHttpException {
        OldHistoryDto target;

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

        return ResponseEntity.ok(OldHistoryResponse.from(target));
    }

    @PostMapping("/histories/reserve")
    public ResponseEntity<OldHistoryResponse> postReserveHistory(@RequestHeader("user-token") String userToken, @RequestBody OldItemRequest itemRequest) throws BadRequestHttpException, InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, ConflictHttpException {
        if(itemRequest == null) {
            throw new BadRequestHttpException("'stuff_name'은 필수입니다.('item_num'은 Optional 입니다)");
        }

        OldHistoryDto savedHistory = null;
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
        return ResponseEntity.created(location).body(OldHistoryResponse.from(savedHistory).toHistoryResponseWithItemWithoutLastHistory());
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/lost")
    public ResponseEntity<OldHistoryResponse> postLostHistory(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum) throws InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException, NotFoundHttpException, MethodNotAllowedHttpException, ConflictHttpException {
        OldHistoryDto savedHistoryDto;
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
        return ResponseEntity.ok(OldHistoryResponse.from(savedHistoryDto).toHistoryResponseWithItemWithoutLastHistory());
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/cancel")
    public ResponseEntity<OldHistoryResponse> patchHistoryToCancel(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, NotFoundHttpException {
        OldHistoryDto updatedHistory;
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
        return ResponseEntity.ok(OldHistoryResponse.from(updatedHistory).toHistoryResponseWithItemWithoutLastHistory());
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/approve")
    public ResponseEntity<OldHistoryResponse> patchHistoryToApprove(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, NotFoundHttpException {
        OldHistoryDto updatedHistory;
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
        return ResponseEntity.ok(OldHistoryResponse.from(updatedHistory).toHistoryResponseWithItemWithoutLastHistory());
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/return")
    public ResponseEntity<OldHistoryResponse> patchHistoryToReturn(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, NotFoundHttpException {
        OldHistoryDto updatedHistory;
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
        return ResponseEntity.ok(OldHistoryResponse.from(updatedHistory).toHistoryResponseWithItemWithoutLastHistory());
    }

    @PatchMapping("/stuffs/{stuffName}/items/{itemNum}/histories/{historyNum}/found")
    public ResponseEntity<OldHistoryResponse> patchHistoryToFound(@RequestHeader("user-token") String userToken, @PathVariable String stuffName, @PathVariable int itemNum, @PathVariable int historyNum) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, MethodNotAllowedHttpException, NotFoundHttpException {
        OldHistoryDto updatedHistory;
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
        return ResponseEntity.ok(OldHistoryResponse.from(updatedHistory).toHistoryResponseWithItemWithoutLastHistory());
    }

    private List<OldHistoryResponse> toHistoryResponse(List<OldHistoryDto> historyDtoList) {
        List<OldHistoryResponse> historyResponseList = new ArrayList<>();
        for(int i = 0; i < historyDtoList.size(); i++) {
            historyResponseList.add(OldHistoryResponse.from(historyDtoList.get(i)));
        }
        return historyResponseList;
    }
}