package com.example.beliemeserver.web.responsebody;

public class JsonResponse {
    private final boolean doesJsonInclude;

    protected JsonResponse(boolean doesJsonInclude) {
        this.doesJsonInclude = doesJsonInclude;
    }

    public boolean doesJsonInclude() {
        return doesJsonInclude;
    }
}
