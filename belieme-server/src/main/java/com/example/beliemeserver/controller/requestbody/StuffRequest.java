package com.example.beliemeserver.controller.requestbody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StuffRequest {
    private String name;
    private String emoji;
    private int amount;
}
