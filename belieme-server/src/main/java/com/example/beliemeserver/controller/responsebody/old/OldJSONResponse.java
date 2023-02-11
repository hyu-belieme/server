package com.example.beliemeserver.controller.responsebody.old;

public class OldJSONResponse {
    private boolean doesJsonInclude;

    public OldJSONResponse(boolean doesJsonInclude) {
        this.doesJsonInclude = doesJsonInclude;
    }

    public boolean doesJsonInclude() {
        return doesJsonInclude;
    }
}
