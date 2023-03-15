package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.domain.dto.UniversityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "university")
@NoArgsConstructor
@Getter
public class UniversityEntity extends DataEntity {
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

    public UniversityEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public UniversityEntity setName(String name) {
        this.name = name;
        return this;
    }

    public UniversityEntity setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public UniversityDto toUniversityDto() {
        return new UniversityDto(
                code,
                name,
                apiUrl
        );
    }
}
