package com.belieme.apiserver.data.entity;

import com.belieme.apiserver.domain.dto.UniversityDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "UNIVERSITY_", uniqueConstraints = {
    @UniqueConstraint(name = "university_index", columnNames = {"name"})})
@NoArgsConstructor
@ToString
@Getter
public class UniversityEntity extends DataEntity<UUID> {

  @Id
  @NonNull
  @Column(name = "id", columnDefinition = "BINARY(16)")
  private UUID id;

  @NonNull
  @Column(name = "name")
  private String name;

  @Column(name = "api_url")
  private String apiUrl;

  public UniversityEntity(@NonNull UUID id, @NonNull String name, String apiUrl) {
    this.id = id;
    this.name = name;
    this.apiUrl = apiUrl;
  }

  public UniversityEntity withName(@NonNull String name) {
    return new UniversityEntity(id, name, apiUrl);
  }

  public UniversityEntity withApiUrl(String apiUrl) {
    return new UniversityEntity(id, name, apiUrl);
  }

  public UniversityDto toUniversityDto() {
    return new UniversityDto(id, name, apiUrl);
  }
}
