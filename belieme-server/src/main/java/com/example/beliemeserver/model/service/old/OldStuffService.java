package com.example.beliemeserver.model.service.old;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dao.old.ItemDao;
import com.example.beliemeserver.model.dao.old.StuffDao;
import com.example.beliemeserver.model.dao.old.UserDao;
import com.example.beliemeserver.model.dto.old.OldItemDto;
import com.example.beliemeserver.model.dto.old.OldStuffDto;
import com.example.beliemeserver.model.dto.old.OldUserDto;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.model.util.old.OldAuthCheck;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OldStuffService {
    private final StuffDao stuffDao;

    private final ItemDao itemDao;

    private final OldAuthCheck authCheck;

    public OldStuffService(StuffDao stuffDao, ItemDao itemDao, UserDao userDao) {
        this.stuffDao = stuffDao;
        this.itemDao = itemDao;
        this.authCheck = new OldAuthCheck(userDao);
    }

    public List<OldStuffDto> getStuffs(String userToken) throws DataException, UnauthorizedException, ForbiddenException {
        OldUserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasUserPermission(requester);

        return stuffDao.getStuffsData();
    }

    public OldStuffDto getStuffByName(String userToken, String name) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException {
        OldUserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        return stuffDao.getStuffByNameData(name);
    }


    public List<OldStuffDto> addStuff(String userToken, String name, String emoji, Integer amount) throws DataException, UnauthorizedException, ForbiddenException, BadRequestException, ConflictException, ItCannotBeException {
        OldUserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        if(amount != null && amount <= 0) {
            throw new BadRequestException("amount는 음수가 될 수 없습니다.");
        }

        OldStuffDto newStuff = OldStuffDto.builder()
                .name(name)
                .emoji(emoji)
                .build();
        OldStuffDto savedStuff = stuffDao.addStuffData(newStuff);

        int realAmount = 1;
        if(amount != null) {
            realAmount = amount;
        }
        for(int i = 0; i < realAmount; i++) {
            OldItemDto newItem = OldItemDto.builder()
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

    public OldStuffDto updateStuff(String userToken, String name, String newName, String newEmoji) throws DataException, UnauthorizedException, ForbiddenException, BadRequestException, NotFoundException {
        OldUserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        if(newName == null && newEmoji == null) {
            throw new BadRequestException("정보가 부족합니다.\n필요한 정보 : name(String), emoji(String) 중 하나 이상");
        }

        OldStuffDto newStuff = stuffDao.getStuffByNameData(name);

        if(newName != null) {
            newStuff.setName(newName);
        }
        if(newEmoji != null) {
            newStuff.setEmoji(newEmoji);
        }

        return stuffDao.updateStuffData(name, newStuff);
    }
}
