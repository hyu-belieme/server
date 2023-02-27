package com.example.beliemeserver.controller.api.old;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.old.OldStuffRequest;
import com.example.beliemeserver.controller.responsebody.old.OldItemResponse;
import com.example.beliemeserver.controller.responsebody.old.OldStuffResponse;
import com.example.beliemeserver.common.Globals;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dto.old.OldItemDto;
import com.example.beliemeserver.model.dto.old.OldStuffDto;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.model.service.old.OldItemService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/old/stuffs/{name}/items")
public class OldItemApiController {
    private final OldItemService itemService;

    public OldItemApiController(OldItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public ResponseEntity<List<OldItemResponse>> getItemsByStuffName(@RequestHeader("user-token") String userToken, @PathVariable String name) throws InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException {
        List<OldItemDto> itemDtoList;
        try {
            itemDtoList = itemService.getItems(userToken, name);
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

        return ResponseEntity.ok(toItemResponseList(itemDtoList));
    }

    @PostMapping("/")
    public ResponseEntity<OldStuffResponse> postOneItem(@RequestHeader("user-token") String userToken, @PathVariable String name, @RequestBody OldStuffRequest request) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, NotFoundHttpException, ConflictHttpException {
        URI location = Globals.getLocation(Globals.serverUrl + "/stuffs/" + name + "/items");

        OldStuffDto updatedStuff;
        try {
            if (request == null) {
                updatedStuff = itemService.postItem(userToken, name, null);
            } else {
                updatedStuff = itemService.postItem(userToken, name, request.getAmount());
            }
        } catch (DataException e) {
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
        } catch (ConflictException e) {
            e.printStackTrace();
            throw new ConflictHttpException(e);
        }

        return ResponseEntity.created(location).body(OldStuffResponse.from(updatedStuff));
    }

    private List<OldItemResponse> toItemResponseList(List<OldItemDto> allItemDtoList) {
        List<OldItemResponse> allItemResponseList = new ArrayList<>();
        for(int i = 0; i < allItemDtoList.size(); i++) {
            allItemResponseList.add(OldItemResponse.from(allItemDtoList.get(i)));
        }
        return allItemResponseList;
    }
}
