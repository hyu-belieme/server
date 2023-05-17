package com.belieme.apiserver.data.repository.custom;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class RefreshRepositoryImpl<T, ID extends Serializable> extends
    SimpleJpaRepository<T, ID> implements RefreshRepository<T, ID> {

  private final EntityManager entityManager;

  public RefreshRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public void refresh(T t) {
    entityManager.refresh(t);
  }
}
