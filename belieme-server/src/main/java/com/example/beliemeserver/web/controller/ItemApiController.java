package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.service.ItemService;
import com.example.beliemeserver.web.responsebody.ItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.department}/${api.keyword.department-index}/${api.keyword.stuff}/${api.keyword.stuff-index}")
public class ItemApiController extends BaseApiController {
    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/${api.keyword.item}")
    public ResponseEntity<List<ItemResponse>> getAllItemsOfStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());

        List<ItemDto> itemDtoList = itemService.getListByStuff(
                userToken, universityCode, departmentCode, stuffName);
        List<ItemResponse> responseList = toResponseList(itemDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.item}/${api.keyword.item-index}")
    public ResponseEntity<ItemResponse> getItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        ItemDto itemDto = itemService.getByIndex(
                userToken, universityCode, departmentCode, stuffName, itemNum);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.keyword.item}")
    public ResponseEntity<ItemResponse> createNewItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());

        ItemDto itemDto = itemService.create(
                userToken, universityCode, departmentCode, stuffName);
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
