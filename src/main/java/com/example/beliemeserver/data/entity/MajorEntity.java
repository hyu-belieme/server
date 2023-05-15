package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.domain.dto.MajorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "MAJOR_", uniqueConstraints = {
        @UniqueConstraint(
                name = "major_index",
                columnNames = {"university_id", "code"}
        )
})
@NoArgsConstructor
@Getter
public class MajorEntity extends DataEntity<UUID> {
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
    private UniversityEntity university;

    public MajorEntity(@NonNull UUID id, @NonNull UniversityEntity university, @NonNull String code) {
        this.id = id;
        this.university = university;
        this.universityId = university.getId();
        this.code = code;
    }

    public MajorEntity withUniversity(@NonNull UniversityEntity university) {
        return new MajorEntity(id, university, code);
    }

    public MajorEntity withCode(@NonNull String code) {
        return new MajorEntity(id, university, code);
    }

    public MajorDto toMajorDto() {
        return new MajorDto(
                id,
                university.toUniversityDto(),
                code
        );
    }
}
