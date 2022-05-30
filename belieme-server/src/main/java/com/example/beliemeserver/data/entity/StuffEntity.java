package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.*;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "stuff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Accessors(chain = true)
@IdClass(StuffId.class)
public class StuffEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "emoji")
    private String emoji;
}
