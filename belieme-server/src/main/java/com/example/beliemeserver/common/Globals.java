package com.example.beliemeserver.common;

import com.example.beliemeserver.controller.httpexception.InternalServerErrorHttpException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Globals {
    public static final String serverUrl = "https://belieme.herokuapp.com";
    public static final String developerApiToken = "c305ee87-a4c7-4b5a-8d71-7e23b6064613";

    public static final String HANYANG_UNIVERSITY_CODE = "HYU";
    public static final String HANYANG_API_CLIENT_TOKEN = "a4b1abe746f384c3d43fa82a17f222";
    public static final String HANYANG_API_URL = "https://api.hanyang.ac.kr/rs/user/loginInfo.json";

    public static URI getLocation(String path) throws InternalServerErrorHttpException {
        try {
            return new URI(Globals.serverUrl + path);
        } catch(URISyntaxException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException("Location of response does not meet URI syntax.");
        }
    }
}