package com.example.beliemeserver.domain.dto._new;

import lombok.NonNull;

import java.util.UUID;

public record UniversityDto(
        @NonNull UUID id,
        @NonNull String name,
        String apiUrl
) {
    private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final UniversityDto nestedEndpoint = new UniversityDto(NIL_UUID,"-", "-");

    public static UniversityDto init(@NonNull String name, String apiUrl) {
        return new UniversityDto(UUID.randomUUID(), name, apiUrl);
    }

    public UniversityDto withName(@NonNull String name) {
        return new UniversityDto(id, name, apiUrl);
    }

    public UniversityDto withApiUrl(String apiUrl) {
        return new UniversityDto(id, name, apiUrl);
    }

    public boolean matchUniqueKey(String name) {
        return this.name.equals(name);
    }

    public boolean matchUniqueKey(UniversityDto oth) {
        if (oth == null) {
            return false;
        }
        return matchUniqueKey(oth.name);
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }
        return "UniversityDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                '}';
    }
}