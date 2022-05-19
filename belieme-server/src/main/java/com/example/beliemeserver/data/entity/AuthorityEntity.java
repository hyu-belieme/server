package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "permission", length = 20)
    private String permission;

    public static AuthorityEntity from(AuthorityDto authorityDto) {
        if(authorityDto == null) {
            return null;
        }
        return new AuthorityEntity(0, authorityDto.getPermission().toString());
    }

    public AuthorityDto toAuthorityDto() throws FormatDoesNotMatchException {
        return AuthorityDto.builder()
                .permission(AuthorityDto.Permission.from(permission))
                .build();
    }

}
