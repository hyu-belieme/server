package com.example.beliemeserver.service;

import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.AuthorityDto;

public interface AuthorityService {
    public AuthorityDto addAuthority(AuthorityDto authorityDto) throws FormatDoesNotMatchException;
}
