package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.model.dto.UniversityDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "university")
@NoArgsConstructor
@Getter
public class UniversityEntity implements DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "code")
    private String code;

    @NonNull
    @Column(name = "name")
    private String name;

    @Column(name = "api_url")
    private String apiUrl;

    public UniversityEntity(String code, String name, String apiUrl) {
        this.code = code;
        this.name = name;
        this.apiUrl = apiUrl;
    }

    public UniversityEntity(int id, String code, String name, String apiUrl) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.apiUrl = apiUrl;
    }

    public UniversityDto toUniversityDto() {
        return new UniversityDto(
                code,
                name,
                apiUrl
        );
    }
}
