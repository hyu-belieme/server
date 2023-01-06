package com.example.beliemeserver.model.dao.old;

import com.example.beliemeserver.model.dto.old.AuthorityDto;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.ConflictException;

public interface AuthorityDao {
    public AuthorityDto addAuthorityData(AuthorityDto authorityDto) throws ConflictException, DataException;

    public AuthorityDto updateAuthorityData(String studentId, AuthorityDto authorityDto) throws DataException;
}
