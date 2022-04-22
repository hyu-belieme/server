package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.requestbody.StuffRequest;
import com.example.beliemeserver.controller.responsebody.StuffResponse;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/stuffs")
public class StuffApiController {
    DummyData dummyData = DummyData.dummyData;

    @GetMapping("/")
    public List<StuffResponse> getAllStuffs(@RequestHeader("user-token") String userToken) {
        return dummyData.stuffList;
    }

    @GetMapping("/{name}")
    public StuffResponse getStuffResponse(@RequestHeader("user-token") String userToken, @PathVariable("name") String name) {
        if(name.equals(dummyData.stuff.getName())) {
            return dummyData.stuff;
        } else {
            return null;
        }
    }

    @PostMapping("/")
    public List<StuffResponse> postStuff(@RequestHeader("user-token") String userToken, @RequestBody StuffRequest newStuff) {
        dummyData.stuffList.add(new StuffResponse(newStuff.getName(), newStuff.getEmoji(), newStuff.getAmount(), newStuff.getAmount(), null));
        return dummyData.stuffList;
    }

    @PatchMapping("/{name}")
    public StuffResponse patchStuff(@RequestHeader("user-token") String userToken, @PathVariable String name, @RequestBody StuffRequest newStuff) {
        if(name.equals(dummyData.stuff.getName())) {
            if(newStuff.getName() != null)
                dummyData.stuff.setName(newStuff.getName());
            if(newStuff.getEmoji() != null)
                dummyData.stuff.setEmoji(newStuff.getEmoji());
        }
        return dummyData.stuff;
    }
}
