package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.exception.*;
import com.example.beliemeserver.model.util.AuthCheck;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StuffService {
    private final StuffDao stuffDao;

    private final ItemDao itemDao;

    private final AuthCheck authCheck;

    public StuffService(StuffDao stuffDao, ItemDao itemDao, UserDao userDao) {
        this.stuffDao = stuffDao;
        this.itemDao = itemDao;
        this.authCheck = new AuthCheck(userDao);
    }

    public List<StuffDto> getStuffs(String userToken) throws DataException, UnauthorizedException, ForbiddenException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasUserPermission(requester);

        return stuffDao.getStuffsData();
    }

    public StuffDto getStuffByName(String userToken, String name) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        return stuffDao.getStuffByNameData(name);
    }


    public List<StuffDto> addStuff(String userToken, String name, String emoji, Integer amount) throws DataException, UnauthorizedException, ForbiddenException, BadRequestException, ConflictException, ItCannotBeException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        if(amount != null && amount <= 0) {
            throw new BadRequestException("amount는 음수가 될 수 없습니다.");
        }

        StuffDto newStuff = StuffDto.builder()
                .name(name)
                .emoji(emoji)
                .build();
        StuffDto savedStuff = stuffDao.addStuffData(newStuff);

        int realAmount = 1;
        if(amount != null) {
            realAmount = amount;
        }
        for(int i = 0; i < realAmount; i++) {
            ItemDto newItem = ItemDto.builder()
                    .stuff(savedStuff)
                    .build();
            try {
                itemDao.addItemData(newItem);
            } catch (NotFoundException e) {
                e.printStackTrace();
                throw new ItCannotBeException();
            }
        }

        return stuffDao.getStuffsData();
    }

    public StuffDto updateStuff(String userToken, String name, String newName, String newEmoji) throws DataException, UnauthorizedException, ForbiddenException, BadRequestException, NotFoundException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        if(newName == null && newEmoji == null) {
            throw new BadRequestException("정보가 부족합니다.\n필요한 정보 : name(String), emoji(String) 중 하나 이상");
        }

        StuffDto newStuff = stuffDao.getStuffByNameData(name);

        if(newName != null) {
            newStuff.setName(newName);
        }
        if(newEmoji != null) {
            newStuff.setEmoji(newEmoji);
        }

        return stuffDao.updateStuffData(name, newStuff);
    }
}
