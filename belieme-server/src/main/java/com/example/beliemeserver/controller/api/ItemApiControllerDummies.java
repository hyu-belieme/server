package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.requestbody.StuffRequest;
import com.example.beliemeserver.controller.responsebody.ItemResponse;
import com.example.beliemeserver.controller.responsebody.StuffResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/stuffs/{name}/items")
public class ItemApiControllerDummies {
    @PostMapping("/")
    public StuffResponse postOneItemDummies(@PathVariable String name, @RequestBody Optional<StuffRequest> request) {
        DummyData dummyData = DummyData.dummyData;
        if(!name.equals(dummyData.stuff.getName())) {
            return null;
        }
        List<ItemResponse> itemList = dummyData.stuff.getItemList();
        if(!request.isPresent() || request.get().getAmount() == 0) {
            itemList.add(new ItemResponse(null, null, itemList.size()+1, null));
            dummyData.stuff.setAmount(dummyData.stuff.getAmount()+1);
            dummyData.stuff.setCount(dummyData.stuff.getCount()+1);
            return dummyData.stuff;
        }
        for(int i = 0; i < request.get().getAmount(); i++) {
            itemList.add(new ItemResponse(null, null, itemList.size() + 1, null));
        }
        dummyData.stuff.setAmount(dummyData.stuff.getAmount()+request.get().getAmount());
        dummyData.stuff.setCount(dummyData.stuff.getCount()+request.get().getAmount());
        return dummyData.stuff;
    }
}
