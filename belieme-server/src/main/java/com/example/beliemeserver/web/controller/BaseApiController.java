package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.config.api.ApiConfig;
import com.example.beliemeserver.web.exception.InvalidUuidFormatException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class BaseApiController {
    @Autowired
    protected ApiConfig api;

    protected UUID toUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUuidFormatException();
        }
    }
}
