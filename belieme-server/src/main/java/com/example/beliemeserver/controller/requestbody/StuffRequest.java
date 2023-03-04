package com.example.beliemeserver.controller.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class StuffRequest {
    @Size(min= 1, max= 30, message = "1 글자에서 30 글자까지 허용됩니다.")
    @Pattern(regexp = "[\\p{L}\\d() ]+", message = "문자와 숫자 그리고 공백만 허용됩니다.")
    @JsonProperty("name")
    String name;

    @Size(min= 1, max= 5, message = "1 글자에서 5 글자까지 허용됩니다.")
    @JsonProperty("emoji")
    String emoji;

    @PositiveOrZero(message = "음수는 허용되지 않습니다.")
    @JsonProperty("amount")
    Integer amount;
}
