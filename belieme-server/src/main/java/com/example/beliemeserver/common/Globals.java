package com.example.beliemeserver.common;

import com.example.beliemeserver.controller.httpexception.InternalServerErrorHttpException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.dto.UserDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Globals {
    public static final String serverUrl = "https://belieme.herokuapp.com";
    public static final String developerApiToken = "c305ee87-a4c7-4b5a-8d71-7e23b6064613";

    public static final UniversityDto DEV_UNIVERSITY = new UniversityDto("DEV", "DEV", "DEV");
    public static final DepartmentDto DEV_DEPARTMENT = new DepartmentDto(DEV_UNIVERSITY, "DEV", "DEV", new ArrayList<>());
    public static final AuthorityDto DEV_AUTHORITY = new AuthorityDto(DEV_DEPARTMENT, AuthorityDto.Permission.DEVELOPER);

    public static final UniversityDto HANYANG_UNIVERSITY = new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/rs/user/loginInfo.json");
    public static final String HANYANG_API_CLIENT_TOKEN = "a4b1abe746f384c3d43fa82a17f222";
    public static final String HANYANG_API_URL = "https://api.hanyang.ac.kr/rs/user/loginInfo.json";

    public static final List<UniversityDto> universities = List.of(
            DEV_UNIVERSITY,
            HANYANG_UNIVERSITY
    );

    public static URI getLocation(String path) throws InternalServerErrorHttpException {
        try {
            return new URI(Globals.serverUrl + path);
        } catch(URISyntaxException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException("Location of response does not meet URI syntax.");
        }
    }
}