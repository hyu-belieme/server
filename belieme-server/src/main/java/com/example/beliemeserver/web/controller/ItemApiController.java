package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto.HistoryDto;
import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.service.HistoryService;
import com.example.beliemeserver.domain.service.ItemService;
import com.example.beliemeserver.web.requestbody.ItemRequest;
import com.example.beliemeserver.web.responsebody.HistoryResponse;
import com.example.beliemeserver.web.responsebody.ItemResponse;
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
    private final ItemService itemService;
    private final HistoryService historyService;

    public ItemApiController(ItemService itemService, HistoryService historyService) {
        this.itemService = itemService;
        this.historyService = historyService;
    }

    @GetMapping("/${api.keyword.item}")
    public ResponseEntity<List<ItemResponse>> getAllItemsOfStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @RequestParam(value = "${api.query.stuff-id}") String stuffId
    ) {
        List<ItemDto> itemDtoList = itemService.getListByStuff(
                userToken, toUUID(stuffId));
        List<ItemResponse> responseList = toResponseList(itemDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.item}/${api.keyword.item-index}")
    public ResponseEntity<ItemResponse> getItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID itemId = toUUID(params.get(api.variable().itemIndex()));

        ItemDto itemDto = itemService.getById(userToken, itemId);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.keyword.item}")
    public ResponseEntity<ItemResponse> createNewItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @RequestBody @Validated ItemRequest newItem
    ) {
        UUID stuffId = toUUID(newItem.getStuffId());
        ItemDto itemDto = itemService.create(userToken, stuffId);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-reserve}")
    public ResponseEntity<HistoryResponse> reserveItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID itemId = toUUID(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.createReservationOnItem(userToken, itemId);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-lost}")
    public ResponseEntity<HistoryResponse> makeItemLost(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID itemId = toUUID(params.get(api.variable().itemIndex()));
        HistoryDto historyDto = historyService.makeItemLost(userToken, itemId);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-approve}")
    public ResponseEntity<HistoryResponse> makeItemApprove(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID itemId = toUUID(params.get(api.variable().itemIndex()));
        HistoryDto historyDto = historyService.makeItemUsing(userToken, itemId);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-return}")
    public ResponseEntity<HistoryResponse> makeItemReturn(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID itemId = toUUID(params.get(api.variable().itemIndex()));
        HistoryDto historyDto = historyService.makeItemReturn(userToken, itemId);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-cancel}")
    public ResponseEntity<HistoryResponse> makeItemCancel(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID itemId = toUUID(params.get(api.variable().itemIndex()));
        HistoryDto historyDto = historyService.makeItemCancel(userToken, itemId);
        HistoryResponse response = HistoryResponse.from(historyDto);
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
