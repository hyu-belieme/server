package com.example.beliemeserver.controller.util;

import com.example.beliemeserver.controller.httpexception.InternalServerErrorHttpException;

import java.net.URI;
import java.net.URISyntaxException;

public class Globals {
    public static final String serverUrl = "https://belieme.herokuapp.com";
    public static final String developerApiToken = "c305ee87-a4c7-4b5a-8d71-7e23b6064613";

    public static URI getLocation(String path) throws InternalServerErrorHttpException {
        try {
            return new URI(Globals.serverUrl + path);
        } catch(URISyntaxException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException("Location of response does not meet URI syntax.");
        }
    }
}