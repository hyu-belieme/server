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
@Builder
@Accessors(chain = true)
public class AuthorityEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "permission", length = 20)
    private String permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public AuthorityDto toAuthorityDto() throws FormatDoesNotMatchException {
        return AuthorityDto.builder()
                .id(id)
                .permission(AuthorityDto.Permission.from(permission))
                .userDto(user.toUserDto())
                .build();
    }

    public AuthorityDto toAuthorityDtoNestedToUser() throws FormatDoesNotMatchException {
        return AuthorityDto.builder()
                .id(id)
                .permission(AuthorityDto.Permission.from(permission))
                .userDto(null)
                .build();
    }
}
