package com.belieme.apiserver.data.repository.custom;

import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RefreshRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

  void refresh(T t);
}
