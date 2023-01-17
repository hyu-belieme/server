package com.example.beliemeserver.model.dao.old;

import com.example.beliemeserver.model.dto.old.OldAuthorityDto;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.exception.ConflictException;

public interface AuthorityDao {
    public OldAuthorityDto addAuthorityData(OldAuthorityDto authorityDto) throws ConflictException, DataException;

    public OldAuthorityDto updateAuthorityData(String studentId, OldAuthorityDto authorityDto) throws DataException;
}
