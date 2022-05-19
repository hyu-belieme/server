package com.example.beliemeserver.service;

import com.example.beliemeserver.data.entity.AuthorityEntity;
import com.example.beliemeserver.data.repository.AuthorityRepository;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public AuthorityDto addAuthority(AuthorityDto authorityDto) throws FormatDoesNotMatchException {
        return authorityRepository.save(AuthorityEntity.from(authorityDto)).toAuthorityDto();
    }
}
