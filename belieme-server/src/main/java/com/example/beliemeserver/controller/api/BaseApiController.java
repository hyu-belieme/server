package com.example.beliemeserver.controller.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:urls/url.properties")
public abstract class BaseApiController {
    @Value("${parameter.universityIndexTag}")
    protected String universityIndexTag;

    @Value("${parameter.departmentIndexTag}")
    protected String departmentIndexTag;

    @Value("${parameter.userIndexTag}")
    protected String userIndexTag;

    @Value("${parameter.stuffIndexTag}")
    protected String stuffIndexTag;

    @Value("${parameter.itemIndexTag}")
    protected String itemIndexTag;

    @Value("${parameter.historyIndexTag}")
    protected String historyIndexTag;
}
