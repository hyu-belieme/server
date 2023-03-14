package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.config.api.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseApiController {
    @Autowired
    protected ApiConfig api;
}
