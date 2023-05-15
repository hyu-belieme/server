package com.belieme.apiserver.data.entity;

import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.AuthorityDto;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "USER_", uniqueConstraints = {
        @UniqueConstraint(
                name = "user_index",
                columnNames = {"university_id", "student_id"}
        )
})
@NoArgsConstructor
@Getter
public class UserEntity extends DataEntity<UUID> {
    @Id
    @NonNull
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "university_id", columnDefinition = "BINARY(16)")
    private UUID universityId;

    @NonNull
    @Column(name = "student_id")
    private String studentId;

    @NonNull
    @Column(name = "name")
    private String name;

    @Column(name = "entrance_year")
    private int entranceYear;

    @NonNull
    @Column(name = "token")
    private String token;

    @Column(name = "created_at")
    private long createdAt;

    @Column(name = "approved_at")
    private long approvedAt;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UniversityEntity university;

    @NonNull
    @OneToMany(mappedBy = "userId")
    private List<AuthorityUserJoinEntity> authorityJoin;

    public UserEntity(
            @NonNull UUID id, @NonNull UniversityEntity university,
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

    private UserEntity(
            @NonNull UUID id, @NonNull UniversityEntity university,
            @NonNull String studentId, @NonNull String name, int entranceYear,
            @NonNull String token, long createdAt, long approvedAt,
            @NonNull List<AuthorityUserJoinEntity> authorityJoin
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
        this.authorityJoin = new ArrayList<>(authorityJoin);
    }

    public List<AuthorityUserJoinEntity> getAuthorityJoin() {
        return new ArrayList<>(authorityJoin);
    }

    public UserEntity withUniversity(@NonNull UniversityEntity university) {
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public UserEntity withStudentId(@NonNull String studentId) {
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public UserEntity withName(@NonNull String name) {
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public UserEntity withEntranceYear(int entranceYear) {
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public UserEntity withToken(@NonNull String token) {
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public UserEntity withCreatedAt(long createdAt) {
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public UserEntity withApprovedAt(long approvedAt) {
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public UserEntity withAuthorityJoin(List<AuthorityUserJoinEntity> authorityJoin) {
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, new ArrayList<>(authorityJoin));
    }

    public UserEntity withAuthorityAdd(@NonNull AuthorityUserJoinEntity authority) {
        List<AuthorityUserJoinEntity> newJoins = new ArrayList<>(authorityJoin);
        newJoins.add(authority);
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, newJoins);
    }

    public UserEntity withAuthorityClear() {
        List<AuthorityUserJoinEntity> newJoins = new ArrayList<>(authorityJoin);
        newJoins.clear();
        return new UserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, newJoins);
    }

    public UserDto toUserDto() {
        List<AuthorityDto> authorityDtoList = new ArrayList<>();
        for (AuthorityUserJoinEntity authority : authorityJoin) {
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
