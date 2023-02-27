package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(
                name = "user_index",
                columnNames = {"university_id", "student_id"}
        )
})
@NoArgsConstructor
@ToString
@Getter
@Accessors(chain = true)
public class UserEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "university_id")
    private int universityId;

    @NonNull
    @Setter
    @Column(name = "student_id")
    private String studentId;

    @NonNull
    @Setter
    @Column(name = "name")
    private String name;

    @NonNull
    @Setter
    @Column(name = "token")
    private String token;

    @Setter
    @Column(name = "create_time_stamp")
    private long createTimeStamp;

    @Setter
    @Column(name = "approval_time_stamp")
    private long approvalTimeStamp;

    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UniversityEntity university;

    @NonNull
    @OneToMany(mappedBy = "userId")
    private List<AuthorityUserJoinEntity> authorityJoin;

    public UserEntity(UniversityEntity university, String studentId, String name, String token, long createTimeStamp, long approvalTimeStamp) {
        this.university = university;
        this.universityId = university.getId();
        this.studentId = studentId;
        this.name = name;
        this.token = token;
        this.createTimeStamp = createTimeStamp;
        this.approvalTimeStamp = approvalTimeStamp;
        this.authorityJoin = new ArrayList<>();
    }

    public UserEntity setUniversity(UniversityEntity university) {
        this.university = university;
        this.universityId = university.getId();
        return this;
    }

    public void addAuthority(AuthorityUserJoinEntity authority) {
        this.authorityJoin.add(authority);
    }

    public void removeAuthority(AuthorityUserJoinEntity authority) {
        this.authorityJoin.remove(authority);
    }

    public UserDto toUserDto() throws FormatDoesNotMatchException {
        List<AuthorityDto> authorityDtoList = new ArrayList<>();
        for(AuthorityUserJoinEntity authority : authorityJoin) {
            authorityDtoList.add(authority.getAuthority().toAuthorityDto());
        }

        return new UserDto(
                university.toUniversityDto(),
                studentId,
                name,
                token,
                createTimeStamp,
                approvalTimeStamp,
                authorityDtoList
        );
    }
}
