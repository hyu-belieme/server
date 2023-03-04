package com.example.beliemeserver.controller.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class DepartmentRequest {
    @Size(min= 1, max= 10, message = "1 글자에서 30 글자까지 허용됩니다.")
    @Pattern(regexp = "[A-Z]+", message = "알파벳 대문자로만 이루어져야 합니다.")
    @JsonProperty("code")
    String code;

    @Size(min= 1, max= 30, message = "1 글자에서 30 글자까지 허용됩니다.")
    @Pattern(regexp = "[\\p{L}\\d() ]+", message = "문자와 숫자 그리고 공백만 허용됩니다.")
    @JsonProperty("name")
    String name;

    @JsonProperty("base-majors")
    List<String> baseMajors;
}
