package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
import lombok.*;

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
public class NewUserEntity extends NewDataEntity<UUID> {
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

    private NewUserEntity(
            @NonNull UUID id, @NonNull NewUniversityEntity university,
            @NonNull String studentId, @NonNull String name, int entranceYear,
            @NonNull String token, long createdAt, long approvedAt,
            @NonNull List<NewAuthorityUserJoinEntity> authorityJoin
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

    public List<NewAuthorityUserJoinEntity> getAuthorityJoin() {
        return new ArrayList<>(authorityJoin);
    }

    public NewUserEntity withUniversity(@NonNull NewUniversityEntity university) {
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public NewUserEntity withStudentId(@NonNull String studentId) {
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public NewUserEntity withName(@NonNull String name) {
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public NewUserEntity withEntranceYear(int entranceYear) {
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public NewUserEntity withToken(@NonNull String token) {
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public NewUserEntity withCreatedAt(long createdAt) {
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public NewUserEntity withApprovedAt(long approvedAt) {
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, authorityJoin);
    }

    public NewUserEntity withAuthorityJoin(List<NewAuthorityUserJoinEntity> authorityJoin) {
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, new ArrayList<>(authorityJoin));
    }

    public NewUserEntity withAuthorityAdd(@NonNull NewAuthorityUserJoinEntity authority) {
        List<NewAuthorityUserJoinEntity> newJoins = new ArrayList<>(authorityJoin);
        newJoins.add(authority);
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, newJoins);
    }

    public NewUserEntity withAuthorityClear() {
        List<NewAuthorityUserJoinEntity> newJoins = new ArrayList<>(authorityJoin);
        newJoins.clear();
        return new NewUserEntity(id, university, studentId, name, entranceYear, token, createdAt, approvedAt, newJoins);
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
