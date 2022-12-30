package com.example.beliemeserver.data.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "department")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class DepartmentEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "university_code")
    private String universityCode;

    @ManyToOne
    @JoinColumn(name = "university_code", referencedColumnName = "code", insertable = false, updatable = false)
    private UniversityEntity university;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
