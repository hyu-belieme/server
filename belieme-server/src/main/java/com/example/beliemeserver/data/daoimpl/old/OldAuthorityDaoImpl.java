package com.example.beliemeserver.data.daoimpl.old;

import com.example.beliemeserver.data.entity.old.OldAuthorityEntity;
import com.example.beliemeserver.data.entity.old.OldUserEntity;
import com.example.beliemeserver.data.entity.id.OldAuthorityId;
import com.example.beliemeserver.data.entity.id.OldUserId;
import com.example.beliemeserver.data.repository.old.OldAuthorityRepository;
import com.example.beliemeserver.model.dao.old.AuthorityDao;
import com.example.beliemeserver.model.dto.old.OldAuthorityDto;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.model.exception.old.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OldAuthorityDaoImpl implements AuthorityDao {
    @Autowired
    OldAuthorityRepository authorityRepository;

    @Override
    public OldAuthorityDto addAuthorityData(OldAuthorityDto authorityDto) throws ConflictException, DataException {
        if(authorityRepository.existsById(new OldAuthorityId(new OldUserId(authorityDto.getUserDto().getStudentId())))) {
            throw new ConflictException();
        }
        OldAuthorityEntity newAuthority = OldAuthorityEntity.builder()
                .user(OldUserEntity.from(authorityDto.getUserDto()))
                .permission(authorityDto.getPermission().toString())
                .build();

        OldAuthorityEntity savedAuthority = authorityRepository.save(newAuthority);
        authorityRepository.refresh(savedAuthority);

        return savedAuthority.toAuthorityDto();
    }

    @Override
    public OldAuthorityDto updateAuthorityData(String studentId, OldAuthorityDto authorityDto) throws DataException {
        OldAuthorityEntity target = authorityRepository.findById(new OldAuthorityId(new OldUserId(studentId))).orElse(null);

        target.setPermission(authorityDto.getPermission().toString());

        OldAuthorityEntity savedAuthority = authorityRepository.save(target);
        authorityRepository.refresh(savedAuthority);

        return savedAuthority.toAuthorityDto();
    }
}
