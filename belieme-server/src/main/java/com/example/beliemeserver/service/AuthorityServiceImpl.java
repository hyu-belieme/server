package com.example.beliemeserver.service;

import com.example.beliemeserver.data.entity.AuthorityEntity;
import com.example.beliemeserver.data.entity.UserEntity;
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
        AuthorityEntity newAuthority = AuthorityEntity.builder()
                .user(UserEntity.from(authorityDto.getUserDto()))
                .permission(authorityDto.getPermission().toString())
                .build();
        System.out.println("user entity" + UserEntity.from(authorityDto.getUserDto()).toString());
        return authorityRepository.save(newAuthority).toAuthorityDto();
    }
}
