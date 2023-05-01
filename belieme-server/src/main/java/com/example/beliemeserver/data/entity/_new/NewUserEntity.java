package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "new_user", uniqueConstraints = {
        @UniqueConstraint(
                name = "new_user_index",
                columnNames = {"university_id", "student_id"}
        )
})
@NoArgsConstructor
@ToString
@Getter
@Accessors(chain = true)
public class NewUserEntity extends NewDataEntity<UUID> {
    @Id
    @NonNull
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "university_id", columnDefinition = "BINARY(16)")
    private UUID universityId;

    @NonNull
    @Setter
    @Column(name = "student_id")
    private String studentId;

    @NonNull
    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "entrance_year")
    private int entranceYear;

    @NonNull
    @Setter
    @Column(name = "token")
    private String token;

    @Setter
    @Column(name = "created_at")
    private long createdAt;

    @Setter
    @Column(name = "approved_at")
    private long approvedAt;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUniversityEntity university;

    @NonNull
    @OneToMany(mappedBy = "userId")
    private List<NewAuthorityUserJoinEntity> authorityJoin;

    public NewUserEntity(
            @NonNull UUID id, @NonNull NewUniversityEntity university,
            @NonNull String studentId, @NonNull String name, int entranceYear,
            @NonNull String token, long createdAt, long approvedAt
    ) {
        this.id = id;
        this.university = university;
        this.universityId = university.getId();
        this.studentId = studentId;
        this.name = name;
        this.entranceYear = entranceYear;
        this.token = token;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.authorityJoin = new ArrayList<>();
    }

    public NewUserEntity setUniversity(@NonNull NewUniversityEntity university) {
        this.university = university;
        this.universityId = university.getId();
        return this;
    }

    public void addAuthority(NewAuthorityUserJoinEntity authority) {
        this.authorityJoin.add(authority);
    }

    public void removeAuthority(NewAuthorityUserJoinEntity authority) {
        this.authorityJoin.remove(authority);
    }

    public UserDto toUserDto() {
        List<AuthorityDto> authorityDtoList = new ArrayList<>();
        for (NewAuthorityUserJoinEntity authority : authorityJoin) {
            authorityDtoList.add(authority.getAuthority().toAuthorityDto());
        }

        return new UserDto(
                id,
                university.toUniversityDto(),
                studentId,
                name,
                entranceYear,
                token,
                createdAt,
                approvedAt,
                authorityDtoList
        );
    }
}
