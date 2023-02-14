package com.example.beliemeserver.controller.responsebody.old;

public class OldResponseFilter {
    // Return true if filtering out (excluding), false to include
    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof OldJSONResponse)) return false;

        OldJSONResponse jsonResponse = (OldJSONResponse) o;
        return !jsonResponse.doesJsonInclude();
    }
}
