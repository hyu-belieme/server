package com.example.beliemeserver.data.entity._new;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "new_authority_user_join")
@NoArgsConstructor
@Getter
@ToString
public class NewAuthorityUserJoinEntity extends NewDataEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(name = "authority_id")
    private int authorityId;

    @NonNull
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userId;

    @NonNull
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewAuthorityEntity authority;

    @NonNull
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUserEntity user;

    public NewAuthorityUserJoinEntity(@NonNull NewAuthorityEntity authority, @NonNull NewUserEntity user) {
        this.authority = authority;
        this.authorityId = authority.getId();
        this.user = user;
        this.userId = user.getId();
    }

    public Integer getId() {
        return id;
    }

    @PreRemove
    private void commitToUserBeforeRemove() {
        user.removeAuthority(this);
    }

    @PrePersist
    private void commitOnUserAfterCreate() {
        user.addAuthority(this);
    }
}