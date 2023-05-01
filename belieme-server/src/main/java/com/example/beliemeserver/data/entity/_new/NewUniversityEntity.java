package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.UniversityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "new_university", uniqueConstraints= {
        @UniqueConstraint(
                name = "new_university_index",
                columnNames = {"name"}
        )
})
@NoArgsConstructor
@Getter
public class NewUniversityEntity extends NewDataEntity<UUID> {
    @Id
    @NonNull
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "name")
    private String name;

    @Column(name = "api_url")
    private String apiUrl;

    public NewUniversityEntity(@NonNull UUID id, @NonNull String name, String apiUrl) {
        this.id = id;
        this.name = name;
        this.apiUrl = apiUrl;
    }

    public NewUniversityEntity setName(@NonNull String name) {
        this.name = name;
        return this;
    }

    public NewUniversityEntity setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public UniversityDto toUniversityDto() {
        return new UniversityDto(id, name, apiUrl);
    }
}
