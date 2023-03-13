package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.common.ApiProperties;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseApiController {
    @Autowired
    protected ApiProperties api;
}
