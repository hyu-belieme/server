package com.example.beliemeserver.controller.api.old;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.old.OldStuffRequest;
import com.example.beliemeserver.controller.responsebody.old.OldStuffResponse;
import com.example.beliemeserver.common.Globals;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.model.service.old.OldStuffService;
import com.example.beliemeserver.model.dto.old.OldStuffDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/old/stuffs")
public class OldStuffApiController {
    private final OldStuffService stuffService;

    public OldStuffApiController(OldStuffService stuffService) {
        this.stuffService = stuffService;
    }

    @GetMapping("/")
    public ResponseEntity<List<OldStuffResponse>> getAllStuffs(@RequestHeader("user-token") String userToken) throws InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException {
        List<OldStuffDto> stuffList;
        try {
            stuffList = stuffService.getStuffs(userToken);
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
        return ResponseEntity.ok(toStuffResponseList(stuffList));
    }

    @GetMapping("/{name}")
    public ResponseEntity<OldStuffResponse> getStuffResponse(@RequestHeader("user-token") String userToken, @PathVariable("name") String name) throws InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException, NotFoundHttpException {
        OldStuffDto target;
        try {
            target = stuffService.getStuffByName(userToken, name);
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
        }

        return ResponseEntity.ok(OldStuffResponse.from(target));
    }

    @PostMapping("/")
    public ResponseEntity<List<OldStuffResponse>> postStuff(@RequestHeader("user-token") String userToken, @RequestBody OldStuffRequest requestBody) throws InternalServerErrorHttpException, UnauthorizedHttpException, ForbiddenHttpException, BadRequestHttpException, ConflictHttpException {
        if(requestBody == null || requestBody.getName() == null || requestBody.getEmoji() == null) {
            throw new BadRequestHttpException("Request body에 정보가 부족합니다.\n필요한 정보 : name(String), emoji(String), amount(int)(optional)");
        }

        URI location = Globals.getLocation(Globals.serverUrl + "/stuffs/" + requestBody.getName());

        List<OldStuffDto> updatedList;
        try {
            updatedList = stuffService.addStuff(userToken, requestBody.getName(), requestBody.getEmoji(), requestBody.getAmount());
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
        } catch (ConflictException e) {
            e.printStackTrace();
            throw new ConflictHttpException(e);
        }

        return ResponseEntity.created(location).body(toStuffResponseList(updatedList));
    }

    @PatchMapping("/{name}")
    public ResponseEntity<OldStuffResponse> patchStuff(@RequestHeader("user-token") String userToken, @PathVariable String name, @RequestBody OldStuffRequest requestBody) throws InternalServerErrorHttpException, BadRequestHttpException, UnauthorizedHttpException, ForbiddenHttpException, NotFoundHttpException {
        if(requestBody == null) {
            throw new BadRequestHttpException("Request body에 정보가 부족합니다.\n필요한 정보 : name(String), emoji(String) 중 하나 이상");
        }

        if(requestBody.getName() != null) {
            URI location = Globals.getLocation(Globals.serverUrl + "/stuffs/" + requestBody.getName());
        }

        OldStuffDto newAndSavedStuff;
        try {
            newAndSavedStuff = stuffService.updateStuff(userToken, name, requestBody.getName(), requestBody.getEmoji());
        } catch (DataException e) {
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
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        }

        return ResponseEntity.ok(OldStuffResponse.from(newAndSavedStuff));
    }

    private List<OldStuffResponse> toStuffResponseList(List<OldStuffDto> allStuffDtoList) {
        List<OldStuffResponse> allStuffResponseList = new ArrayList<>();
        for(int i = 0; i < allStuffDtoList.size(); i++) {
            allStuffResponseList.add(OldStuffResponse.from(allStuffDtoList.get(i)).toStuffResponseWithoutItemList());
        }
        return allStuffResponseList;
    }
}