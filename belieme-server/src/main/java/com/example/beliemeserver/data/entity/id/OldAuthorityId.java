package com.example.beliemeserver.data.entity.id;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OldAuthorityId implements Serializable {
    private OldUserId user;
}
