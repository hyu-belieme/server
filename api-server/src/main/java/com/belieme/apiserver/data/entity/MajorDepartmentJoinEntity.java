package com.belieme.apiserver.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "MAJOR_DEPARTMENT_JOIN_")
@NoArgsConstructor
@Getter
@ToString
public class MajorDepartmentJoinEntity extends DataEntity<Integer> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increase_sequece_generator")
  @SequenceGenerator(name = "auto_increase_sequece_generator", sequenceName = "GLOBAL_AUTO_INCREASE", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @NonNull
  @Column(name = "major_id", columnDefinition = "BINARY(16)")
  private UUID majorId;

  @NonNull
  @ToString.Exclude
  @Column(name = "department_id", columnDefinition = "BINARY(16)")
  private UUID departmentId;

  @NonNull
  @ManyToOne
  @JoinColumn(name = "major_id", referencedColumnName = "id", insertable = false, updatable = false)
  private MajorEntity major;

  @NonNull
  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
  private DepartmentEntity department;

  public MajorDepartmentJoinEntity(@NonNull MajorEntity major,
      @NonNull DepartmentEntity department) {
    this.major = major;
    this.majorId = major.getId();
    this.department = department;
    this.departmentId = department.getId();
  }
}
