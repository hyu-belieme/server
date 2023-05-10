package com.example.beliemeserver.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "authority_user_join")
@NoArgsConstructor
@Getter
@ToString
public class AuthorityUserJoinEntity extends DataEntity<Integer> {
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
    private AuthorityEntity authority;

    @NonNull
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    public AuthorityUserJoinEntity(@NonNull AuthorityEntity authority, @NonNull UserEntity user) {
        this.authority = authority;
        this.authorityId = authority.getId();
        this.user = user;
        this.userId = user.getId();
    }

    public Integer getId() {
        return id;
    }
}
