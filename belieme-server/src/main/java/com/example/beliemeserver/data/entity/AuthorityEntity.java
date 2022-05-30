package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.*;
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
@Builder
@Accessors(chain = true)
@IdClass(AuthorityId.class)
public class AuthorityEntity implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "permission", length = 20)
    private String permission;


    public AuthorityDto toAuthorityDto() throws FormatDoesNotMatchException {
        return AuthorityDto.builder()
                .userDto(user.toUserDto())
                .permission(AuthorityDto.Permission.from(permission))
                .build();
    }

    public AuthorityDto toAuthorityDtoNestedToUser() throws FormatDoesNotMatchException {
        return AuthorityDto.builder()
                .userDto(null)
                .permission(AuthorityDto.Permission.from(permission))
                .build();
    }
}
