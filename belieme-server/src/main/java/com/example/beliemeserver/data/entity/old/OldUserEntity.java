package com.example.beliemeserver.data.entity.old;

import com.example.beliemeserver.data.entity.DataEntity;
import com.example.beliemeserver.data.entity.id.OldUserId;
import com.example.beliemeserver.data.exception.old.OldFormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.old.OldAuthorityDto;
import com.example.beliemeserver.model.dto.old.OldUserDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "old_user")
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@Accessors(chain = true)
@IdClass(OldUserId.class)
public class OldUserEntity extends DataEntity {
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
    private List<OldAuthorityEntity> authorities;

    public OldUserEntity() {
        authorities = new ArrayList<>();
    }

    public OldUserDto toUserDto() throws OldFormatDoesNotMatchException {
        List<OldAuthorityDto> authorityDtoList = new ArrayList<>();
        if(authorities != null) {
            Iterator<OldAuthorityEntity> iterator = authorities.iterator();
            while (iterator.hasNext()) {
                authorityDtoList.add(iterator.next().toAuthorityDtoNestedToUser());
            }
        }

        return new OldUserDto(
                studentId,
                name,
                token,
                createTimeStamp,
                approvalTimeStamp,
                authorityDtoList
        );
    }

    public static OldUserEntity from(OldUserDto userDto) {
        if(userDto == null) {
            return null;
        }
        return OldUserEntity.builder()
                .studentId(userDto.getStudentId())
                .name(userDto.getName())
                .token(userDto.getToken())
                .createTimeStamp(userDto.getCreateTimeStamp())
                .approvalTimeStamp(userDto.getApprovalTimeStamp())
                .build();
    }

    @Override
    public int getId() {
        return 0;
    }
}
