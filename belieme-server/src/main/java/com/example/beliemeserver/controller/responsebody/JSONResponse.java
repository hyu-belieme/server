package com.example.beliemeserver.controller.responsebody;

public class JSONResponse {
    private boolean doesJsonInclude;

    public JSONResponse(boolean doesJsonInclude) {
        this.doesJsonInclude = doesJsonInclude;
    }

    public boolean doesJsonInclude() {
        return doesJsonInclude;
    }
}
