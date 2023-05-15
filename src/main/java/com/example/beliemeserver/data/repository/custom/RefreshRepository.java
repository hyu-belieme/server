package com.example.beliemeserver.data.repository.custom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface RefreshRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    void refresh(T t);
}
