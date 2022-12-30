package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.model.dto.UniversityDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "university")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class UniversityEntity {
    @Id
    @NonNull
    @Column(name = "code")
    private String code;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "api_url")
    private String apiUrl;

    public UniversityDto toUniversityDto() {
        return new UniversityDto(
                code,
                name,
                apiUrl
        );
    }
}
