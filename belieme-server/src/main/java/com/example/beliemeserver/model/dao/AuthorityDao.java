package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.ConflictException;

public interface AuthorityDao {
    public AuthorityDto addAuthorityData(AuthorityDto authorityDto) throws ConflictException, DataException;
}
