package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.service._new.NewItemService;
import com.example.beliemeserver.web.requestbody._new.ItemRequest;
import com.example.beliemeserver.web.responsebody._new.ItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "")
public class ItemApiController extends BaseApiController {
    private final NewItemService itemService;

    public ItemApiController(NewItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/${api.keyword.item}")
    public ResponseEntity<List<ItemResponse>> getAllItemsOfStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @RequestParam(value = "${api.query.stuff-id}") String stuffId
    ) {
        List<ItemDto> itemDtoList = itemService.getListByStuff(
                userToken, getUuidFromString(stuffId));
        List<ItemResponse> responseList = toResponseList(itemDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.item}/${api.keyword.item-index}")
    public ResponseEntity<ItemResponse> getItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID itemId = getUuidFromString(params.get(api.variable().itemIndex()));

        ItemDto itemDto = itemService.getById(userToken, itemId);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.keyword.item}")
    public ResponseEntity<ItemResponse> createNewItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @RequestBody @Validated ItemRequest newItem
    ) {
        UUID stuffId = getUuidFromString(newItem.getStuffId());
        ItemDto itemDto = itemService.create(userToken, stuffId);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(response);
    }

    private List<ItemResponse> toResponseList(List<ItemDto> itemDtoList) {
        List<ItemResponse> responseList = new ArrayList<>();
        for (ItemDto dto : itemDtoList) {
            responseList.add(ItemResponse.from(dto));
        }
        return responseList;
    }
}
