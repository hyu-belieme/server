package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.*;

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
public class UserEntity implements DataEntity {
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
    @OneToMany(mappedBy = "user")
    private List<MajorUserJoinEntity> majorJoin;

    @NonNull
    @OneToMany(mappedBy = "user")
    private List<AuthorityEntity> authorities;

    public UserEntity(UniversityEntity university, String studentId, String name, String token, long createTimeStamp, long approvalTimeStamp) {
        this.university = university;
        this.universityId = university.getId();
        this.studentId = studentId;
        this.name = name;
        this.token = token;
        this.createTimeStamp = createTimeStamp;
        this.approvalTimeStamp = approvalTimeStamp;
        this.authorities = new ArrayList<>();
    }

    public void setUniversity(UniversityEntity university) {
        this.university = university;
        this.universityId = university.getId();
    }

    public void addAuthority(AuthorityEntity authority) {
        this.authorities.add(authority);
    }

    public void removeAuthority(AuthorityEntity authority) {
        this.authorities.remove(authority);
    }

    public void addMajor(MajorUserJoinEntity major) {
        this.majorJoin.add(major);
    }

    public void removeMajor(MajorUserJoinEntity major) {
        this.majorJoin.remove(major);
    }

    public UserDto toUserDto() throws FormatDoesNotMatchException {
        UserDto output = new UserDto(
                university.toUniversityDto(),
                studentId,
                name,
                token,
                createTimeStamp,
                approvalTimeStamp,
                new ArrayList<>(),
                new ArrayList<>()
        );

        for(AuthorityEntity authority : authorities) {
            output.addAuthority(authority.toAuthorityDtoNestedToUser());
        }

        for(MajorUserJoinEntity major : majorJoin) {
            output.addMajor(major.getMajor().toMajorDto());
        }

        return output;
    }
}
