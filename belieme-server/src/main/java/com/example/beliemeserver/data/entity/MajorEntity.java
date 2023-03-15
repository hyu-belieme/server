package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.domain.dto.MajorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "major", uniqueConstraints = {
        @UniqueConstraint(
                name = "major_index",
                columnNames = {"university_id", "code"}
        )
})
@NoArgsConstructor
@Getter
public class MajorEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "university_id")
    private int universityId;

    @NonNull
    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UniversityEntity university;

    public MajorEntity(UniversityEntity university, String code) {
        this.university = university;
        this.universityId = university.getId();
        this.code = code;
    }

    public MajorEntity setUniversity(UniversityEntity university) {
        this.university = university;
        this.universityId = university.getId();
        return this;
    }

    public MajorEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public MajorDto toMajorDto() {
        return new MajorDto(
                university.toUniversityDto(),
                code
        );
    }
}
