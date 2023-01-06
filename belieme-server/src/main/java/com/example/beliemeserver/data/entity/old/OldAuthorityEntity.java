package com.example.beliemeserver.data.entity.old;

import com.example.beliemeserver.data.entity.DataEntity;
import com.example.beliemeserver.data.entity.id.*;
import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.old.OldAuthorityDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "old_authority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@IdClass(OldAuthorityId.class)
public class OldAuthorityEntity implements Serializable, DataEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private OldUserEntity user;

    @Column(name = "permission", length = 20)
    private String permission;


    public OldAuthorityDto toAuthorityDto() throws FormatDoesNotMatchException {
        return OldAuthorityDto.builder()
                .userDto(user.toUserDto())
                .permission(OldAuthorityDto.Permission.from(permission))
                .build();
    }

    public OldAuthorityDto toAuthorityDtoNestedToUser() throws FormatDoesNotMatchException {
        return OldAuthorityDto.builder()
                .userDto(null)
                .permission(OldAuthorityDto.Permission.from(permission))
                .build();
    }
}
