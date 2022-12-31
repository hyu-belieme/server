package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.model.dto.MajorDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "major", uniqueConstraints={
        @UniqueConstraint(
                name = "major_index",
                columnNames={"university_id", "code"}
        )
})
@NoArgsConstructor
@Getter
@Setter
public class MajorEntity implements DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
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

    public MajorEntity(int universityId, String code,  UniversityEntity university) {
        this.universityId = universityId;
        this.code = code;
        this.university = university;
    }

    public MajorEntity(int id, int universityId, String code, UniversityEntity university) {
        this.id = id;
        this.universityId = universityId;
        this.code = code;
        this.university = university;
    }

    public MajorDto toMajorDto() {
        return new MajorDto(
                university.toUniversityDto(),
                code
        );
    }
}
