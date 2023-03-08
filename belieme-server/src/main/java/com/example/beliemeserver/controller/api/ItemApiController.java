package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.ItemResponse;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/${api.university}/${api.universityIndex}/${api.department}/${api.departmentIndex}/${api.stuff}/${api.stuffIndex}")
public class ItemApiController extends BaseApiController {
    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/${api.item}")
    public ResponseEntity<List<ItemResponse>> getAllItemsOfStuff(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);

        List<ItemDto> itemDtoList = itemService.getListByStuff(
                userToken, universityCode, departmentCode, stuffName);
        List<ItemResponse> responseList = toResponseList(itemDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.item}/${api.itemIndex}")
    public ResponseEntity<ItemResponse> getItem(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);
        int itemNum = Integer.parseInt(params.get(itemIndexTag));

        ItemDto itemDto = itemService.getByIndex(
                userToken, universityCode, departmentCode, stuffName, itemNum);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.item}")
    public ResponseEntity<ItemResponse> createNewItem(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);

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
