package com.example.beliemeserver.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "authority_user_join")
@NoArgsConstructor
@Getter
@ToString
public class AuthorityUserJoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NonNull
    @Column(name = "authority_id")
    private int authorityId;

    @NonNull
    @Column(name = "user_id")
    private int userId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", insertable = false, updatable = false)
    private AuthorityEntity authority;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    // TODO 실험
//    @PreRemove
//    private void commitToUserBeforeRemove() {
//        user.removeAuthority(this);
//    }

//    @PrePersist
//    private void commitOnUserAfterCreate() {
//        user.addAuthority(this);
//    }
}
