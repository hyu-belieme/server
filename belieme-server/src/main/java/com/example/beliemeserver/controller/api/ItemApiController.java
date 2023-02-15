package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.ItemResponse;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/universities/{university-code}/departments/{department-code}/stuffs/{stuff-name}")
public class ItemApiController {
    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> getAllItemsOfStuff(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode,
            @PathVariable("stuff-name") String stuffName
    ) {
        List<ItemDto> itemDtoList = itemService.getListByStuff(
                userToken, universityCode, departmentCode, stuffName);
        List<ItemResponse> responseList = toResponseList(itemDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/items/{item-num}")
    public ResponseEntity<ItemResponse> getItem(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode,
            @PathVariable("stuff-name") String stuffName,
            @PathVariable("item-num") int itemNum
    ) {
        ItemDto itemDto = itemService.getByIndex(
                userToken, universityCode, departmentCode, stuffName, itemNum);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<ItemResponse> createNewItem(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode,
            @PathVariable("stuff-name") String stuffName
    ) {
        ItemDto itemDto = itemService.create(
                userToken, universityCode, departmentCode, stuffName);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(response);
    }

    private List<ItemResponse> toResponseList(List<ItemDto> itemDtoList) {
        List<ItemResponse> responseList = new ArrayList<>();
        for(ItemDto dto : itemDtoList) {
            responseList.add(ItemResponse.from(dto));
        }
        return responseList;
    }
}
