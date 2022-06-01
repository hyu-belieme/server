package com.example.beliemeserver.model.dto;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class AuthorityDto {
    private Permission permission;
    private UserDto userDto;

    public enum Permission {
        BANNED, USER, STAFF, MASTER, DEVELOPER;

        public boolean hasUserPermission() {
            switch (this) {
                case DEVELOPER:
                case MASTER:
                case STAFF:
                case USER:
                    return true;
                case BANNED:
                default:
                    return false;
            }
        }

        public boolean hasStaffPermission() {
            switch (this) {
                case DEVELOPER:
                case MASTER:
                case STAFF:
                    return true;
                case USER:
                case BANNED:
                default:
                    return false;
            }
        }

        public boolean hasMasterPermission() {
            switch (this) {
                case DEVELOPER:
                case MASTER:
                    return true;
                case STAFF:
                case USER:
                case BANNED:
                default:
                    return false;
            }
        }

        public boolean hasDeveloperPermission() {
            switch (this) {
                case DEVELOPER:
                    return true;
                case MASTER:
                case STAFF:
                case USER:
                case BANNED:
                default:
                    return false;
            }
        }

        public boolean hasMorePermission(Permission other) {
            switch (this) {
                case BANNED:
                    return true;
                case USER:
                    return other != BANNED && other != USER;
                case STAFF:
                    return other == MASTER || other == DEVELOPER;
                case MASTER:
                    return other == DEVELOPER;
                default:
                    return false;
            }
        }

        public static Permission from(String string) throws FormatDoesNotMatchException {
            switch (string) {
                case "BANNED":
                    return BANNED;
                case "USER":
                    return USER;
                case "STAFF":
                    return STAFF;
                case "MASTER":
                    return MASTER;
                case "DEVELOPER":
                    return DEVELOPER;
                default:
                    throw new FormatDoesNotMatchException();
            }
        }
    }
}
