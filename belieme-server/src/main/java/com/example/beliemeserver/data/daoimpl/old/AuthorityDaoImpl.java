package com.example.beliemeserver.data.daoimpl.old;

import com.example.beliemeserver.data.entity.old.AuthorityEntity;
import com.example.beliemeserver.data.entity.old.UserEntity;
import com.example.beliemeserver.data.entity.id.AuthorityId;
import com.example.beliemeserver.data.entity.id.UserId;
import com.example.beliemeserver.data.repository.old.AuthorityRepository;
import com.example.beliemeserver.model.dao.old.AuthorityDao;
import com.example.beliemeserver.model.dto.old.AuthorityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityDaoImpl implements AuthorityDao {
    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public AuthorityDto addAuthorityData(AuthorityDto authorityDto) throws ConflictException, DataException {
        if(authorityRepository.existsById(new AuthorityId(new UserId(authorityDto.getUserDto().getStudentId())))) {
            throw new ConflictException();
        }
        AuthorityEntity newAuthority = AuthorityEntity.builder()
                .user(UserEntity.from(authorityDto.getUserDto()))
                .permission(authorityDto.getPermission().toString())
                .build();

        AuthorityEntity savedAuthority = authorityRepository.save(newAuthority);
        authorityRepository.refresh(savedAuthority);

        return savedAuthority.toAuthorityDto();
    }

    @Override
    public AuthorityDto updateAuthorityData(String studentId, AuthorityDto authorityDto) throws DataException {
        AuthorityEntity target = authorityRepository.findById(new AuthorityId(new UserId(studentId))).orElse(null);

        target.setPermission(authorityDto.getPermission().toString());

        AuthorityEntity savedAuthority = authorityRepository.save(target);
        authorityRepository.refresh(savedAuthority);

        return savedAuthority.toAuthorityDto();
    }
}
