package com.example.beliemeserver.controller.responsebody;

public class ResponseFilter {
    // Return true if filtering out (excluding), false to include
    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof JSONResponse)) return false;

        JSONResponse jsonResponse = (JSONResponse) o;
        return !jsonResponse.doesJsonInclude();
    }
}
