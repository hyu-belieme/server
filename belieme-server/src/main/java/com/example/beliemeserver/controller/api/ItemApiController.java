package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.StuffRequest;
import com.example.beliemeserver.controller.responsebody.ItemResponse;
import com.example.beliemeserver.controller.responsebody.StuffResponse;
import com.example.beliemeserver.common.Globals;

import com.example.beliemeserver.model.dto.old.OldItemDto;
import com.example.beliemeserver.model.exception.*;
import com.example.beliemeserver.model.dto.old.OldStuffDto;
import com.example.beliemeserver.model.service.ItemService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/stuffs/{name}/items")
public class ItemApiController {
    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ItemResponse>> getItemsByStuffName(@RequestHeader("user-token") String userToken, @PathVariable String name) throws InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException {
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
    public ResponseEntity<StuffResponse> postOneItem(@RequestHeader("user-token") String userToken, @PathVariable String name, @RequestBody StuffRequest request) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, NotFoundHttpException, ConflictHttpException {
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

        return ResponseEntity.created(location).body(StuffResponse.from(updatedStuff));
    }

    private List<ItemResponse> toItemResponseList(List<OldItemDto> allItemDtoList) {
        List<ItemResponse> allItemResponseList = new ArrayList<>();
        for(int i = 0; i < allItemDtoList.size(); i++) {
            allItemResponseList.add(ItemResponse.from(allItemDtoList.get(i)));
        }
        return allItemResponseList;
    }
}
