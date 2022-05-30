package com.example.beliemeserver.model.dto;

import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class AuthorityDto {
    private int id;
    private Permission permission;
    private UserDto userDto;

    public enum Permission {
        BANNED, USER, STAFF, MASTER, DEVELOPER;

        public boolean hasMorePermission(Permission other) {
            switch (this) {
                case BANNED:
                    return true;
                case USER:
                    if (other == BANNED || other == USER) {
                        return false;
                    } else {
                        return true;
                    }
                case STAFF:
                    if (other == MASTER || other == DEVELOPER) {
                        return true;
                    } else {
                        return false;
                    }
                case MASTER:
                    if (other == DEVELOPER) {
                        return true;
                    } else {
                        return false;
                    }
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
