package com.example.beliemeserver.controller.responsebody;

public class ResponseFilter {
    // Return true if filtering out (excluding), false to include
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JsonResponse jsonResponse)) return false;
        return !jsonResponse.doesJsonInclude();
    }
}
