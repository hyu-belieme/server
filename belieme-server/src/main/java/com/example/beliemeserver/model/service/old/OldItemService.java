package com.example.beliemeserver.model.service.old;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
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
public class OldItemService {
    private final StuffDao stuffDao;

    private final ItemDao itemDao;

    private final OldAuthCheck authCheck;

    public OldItemService(StuffDao stuffDao, ItemDao itemDao, UserDao userDao) {
        this.stuffDao = stuffDao;
        this.itemDao = itemDao;
        this.authCheck = new OldAuthCheck(userDao);
    }

    public List<OldItemDto> getItems(String userToken, String stuffName) throws DataException, UnauthorizedException, ForbiddenException {
        OldUserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        return itemDao.getItemsByStuffNameData(stuffName);
    }

    public OldStuffDto postItem(String userToken, String stuffName, Integer amount) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException, ConflictException {
        OldUserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        OldStuffDto targetStuff = stuffDao.getStuffByNameData(stuffName);

        int realAmount = 1;
        if(amount != null) {
            realAmount = amount;
        }

        for (int i = 0; i < realAmount; i++) {
            itemDao.addItemData(
                    OldItemDto.builder()
                            .stuff(targetStuff)
                            .lastHistory(null)
                            .build()
            );
        }

        return stuffDao.getStuffByNameData(stuffName);
    }
}
