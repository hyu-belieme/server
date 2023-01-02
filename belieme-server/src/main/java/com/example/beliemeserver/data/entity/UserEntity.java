package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.UserId;
import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@Accessors(chain = true)
@IdClass(UserId.class)
public class UserEntity implements Serializable, DataEntity {
    @Id
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "name")
    private String name;

    @Column(name = "token")
    private String token;

    @Column(name = "create_time_stamp")
    private long createTimeStamp;

    @Column(name = "approval_time_stamp")
    private long approvalTimeStamp;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<AuthorityEntity> authorities;

    public UserEntity() {
        authorities = new ArrayList<>();
    }

    public UserDto toUserDto() throws FormatDoesNotMatchException {
        List<AuthorityDto> authorityDtoList = new ArrayList<>();
        if(authorities != null) {
            Iterator<AuthorityEntity> iterator = authorities.iterator();
            while (iterator.hasNext()) {
                authorityDtoList.add(iterator.next().toAuthorityDtoNestedToUser());
            }
        }

        return new UserDto(
                studentId,
                name,
                token,
                createTimeStamp,
                approvalTimeStamp,
                authorityDtoList
        );
    }

    public static UserEntity from(UserDto userDto) {
        if(userDto == null) {
            return null;
        }
        return UserEntity.builder()
                .studentId(userDto.getStudentId())
                .name(userDto.getName())
                .token(userDto.getToken())
                .createTimeStamp(userDto.getCreateTimeStamp())
                .approvalTimeStamp(userDto.getApprovalTimeStamp())
                .build();
    }
}
