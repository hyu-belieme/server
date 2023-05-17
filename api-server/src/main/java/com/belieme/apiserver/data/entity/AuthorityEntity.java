package com.belieme.apiserver.data.entity;

import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "AUTHORITY_", uniqueConstraints = {
    @UniqueConstraint(name = "authority_index", columnNames = {"department_id", "permission"})})
@NoArgsConstructor
@ToString
@Getter
public class AuthorityEntity extends DataEntity<Integer> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increase_sequece_generator")
  @SequenceGenerator(name = "auto_increase_sequece_generator", sequenceName = "GLOBAL_AUTO_INCREASE", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @NonNull
  @Column(name = "department_id", columnDefinition = "BINARY(16)")
  private UUID departmentId;

  @NonNull
  @Column(name = "permission", length = 20)
  private String permission;

  @NonNull
  @ManyToOne
  @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
  private DepartmentEntity department;

  public AuthorityEntity(@NonNull DepartmentEntity department, @NonNull String permission) {
    this.department = department;
    this.departmentId = department.getId();
    this.permission = permission; // TODO : validate Permission String
  }

  public AuthorityEntity(int id, @NonNull DepartmentEntity department, @NonNull String permission) {
    this.id = id;
    this.department = department;
    this.departmentId = department.getId();
    this.permission = permission; // TODO : validate Permission String
  }

  public AuthorityDto toAuthorityDto() {
    return new AuthorityDto(department.toDepartmentDto(), Permission.valueOf(permission));
  }
}
