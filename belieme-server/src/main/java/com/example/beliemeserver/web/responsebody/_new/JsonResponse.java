package com.example.beliemeserver.web.responsebody._new;

public class JsonResponse {
    private final boolean doesJsonInclude;

    protected JsonResponse(boolean doesJsonInclude) {
        this.doesJsonInclude = doesJsonInclude;
    }

    public boolean doesJsonInclude() {
        return doesJsonInclude;
    }
}
