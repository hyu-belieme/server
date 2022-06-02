package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.StuffRequest;
import com.example.beliemeserver.controller.responsebody.StuffResponse;
import com.example.beliemeserver.controller.util.Globals;

import com.example.beliemeserver.model.exception.*;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.service.ItemService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path="/stuffs/{name}/items")
public class ItemApiController {
    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/")
    public ResponseEntity<StuffResponse> postOneItem(@RequestHeader("user-token") String userToken, @PathVariable String name, @RequestBody StuffRequest request) throws UnauthorizedHttpException, InternalServerErrorHttpException, ForbiddenHttpException, NotFoundHttpException, ConflictHttpException {
        URI location = Globals.getLocation(Globals.serverUrl + "/stuffs/" + name + "/items");

        StuffDto updatedStuff;
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
}
