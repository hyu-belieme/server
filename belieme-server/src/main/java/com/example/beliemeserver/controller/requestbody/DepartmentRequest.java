package com.example.beliemeserver.controller.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DepartmentRequest {
    @JsonProperty("code")
    String code;
    @JsonProperty("name")
    String name;
    @JsonProperty("base-majors")
    List<String> baseMajors;
}
