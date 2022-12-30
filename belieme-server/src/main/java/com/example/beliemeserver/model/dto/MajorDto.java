package com.example.beliemeserver.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class MajorDto {
    private UniversityDto university;
    private String code;
}
