package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.MajorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "new_major", uniqueConstraints = {
        @UniqueConstraint(
                name = "major_index",
                columnNames = {"university_id", "code"}
        )
})
@NoArgsConstructor
@Getter
public class NewMajorEntity extends NewDataEntity<UUID> {
    @Id
    @NonNull
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "university_id", columnDefinition = "BINARY(16)")
    private UUID universityId;

    @NonNull
    @Column(name = "code")
    private String code;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUniversityEntity university;

    public NewMajorEntity(@NonNull UUID id, @NonNull NewUniversityEntity university, @NonNull String code) {
        this.id = id;
        this.university = university;
        this.universityId = university.getId();
        this.code = code;
    }

    public NewMajorEntity setId(@NonNull UUID id) {
        this.id = id;
        return this;
    }

    public NewMajorEntity setUniversity(@NonNull NewUniversityEntity university) {
        this.university = university;
        this.universityId = university.getId();
        return this;
    }

    public NewMajorEntity setCode(@NonNull String code) {
        this.code = code;
        return this;
    }

    public MajorDto toMajorDto() {
        return new MajorDto(
                id,
                university.toUniversityDto(),
                code
        );
    }
}
