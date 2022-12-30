package com.example.beliemeserver.data.entity.id;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MajorDepartmentJoinId implements Serializable {
    private int majorId;
    private int departmentId;
}
