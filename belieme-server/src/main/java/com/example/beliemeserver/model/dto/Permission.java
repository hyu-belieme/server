package com.example.beliemeserver.model.dto;

public enum Permission {
    DEFAULT, BANNED, USER, STAFF, MASTER, DEVELOPER;

    public boolean hasUserPermission() {
        return switch (this) {
            case DEVELOPER, MASTER, STAFF, USER -> true;
            default -> false;
        };
    }

    public boolean hasStaffPermission() {
        return switch (this) {
            case DEVELOPER, MASTER, STAFF -> true;
            default -> false;
        };
    }

    public boolean hasMasterPermission() {
        return this == DEVELOPER || this == MASTER;
    }

    public boolean hasDeveloperPermission() {
        return this == DEVELOPER;
    }

    public boolean hasMorePermission(Permission other) {
        return switch (this) {
            case DEVELOPER -> true;
            case MASTER -> other != DEVELOPER;
            case STAFF -> other != DEVELOPER && other != MASTER;
            case USER -> other == BANNED || other == USER;
            case BANNED -> other == BANNED;
            default -> false;
        };
    }
}
