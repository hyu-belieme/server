package com.belieme.apiserver.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "AUTHORITY_USER_JOIN_")
@NoArgsConstructor
@Getter
@ToString
public class AuthorityUserJoinEntity extends DataEntity<Integer> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increase_sequece_generator")
  @SequenceGenerator(name = "auto_increase_sequece_generator", sequenceName = "GLOBAL_AUTO_INCREASE", allocationSize = 1)
  @Column(name = "id")
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
