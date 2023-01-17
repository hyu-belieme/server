package com.example.beliemeserver.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "major_user_join")
@NoArgsConstructor
@Getter
@ToString
public class MajorUserJoinEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NonNull
    @Column(name = "major_id")
    private int majorId;

    @NonNull
    @Column(name = "user_id")
    private int userId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MajorEntity major;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    public MajorUserJoinEntity(MajorEntity major, UserEntity user) {
        this.major = major;
        this.majorId = major.getId();
        this.user = user;
        this.userId = user.getId();
    }

    @PreRemove
    private void commitToUserBeforeRemove() {
        user.removeMajor(this);
    }

    @PrePersist
    private void commitOnUserAfterCreate() {
        user.addMajor(this);
    }
}
