package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

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

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission", referencedColumnName = "permission")})
    private Set<AuthorityEntity> authorities;

    public UserDto toUserDto() throws FormatDoesNotMatchException {
        Set<AuthorityDto> authorityDtoSet = new HashSet<>();
        Iterator<AuthorityEntity> iterator = authorities.iterator();
        while(iterator.hasNext()) {
            authorityDtoSet.add(iterator.next().toAuthorityDto());
        }

        return new UserDto(
                studentId,
                name,
                token,
                createTimeStamp,
                approvalTimeStamp,
                authorityDtoSet
        );
    }

    public static UserEntity from(UserDto userDto) {
        Set<AuthorityEntity> authorityEntitySet = new HashSet<>();
        Iterator<AuthorityDto> iterator = userDto.getAuthorities().iterator();
        while(iterator.hasNext()) {
            authorityEntitySet.add(AuthorityEntity.from(iterator.next()));
        }
        return new UserEntity(
                0,
                userDto.getStudentId(),
                userDto.getName(),
                userDto.getToken(),
                userDto.getCreateTimeStamp(),
                userDto.getApprovalTimeStamp(),
                authorityEntitySet
        );
    }
}
