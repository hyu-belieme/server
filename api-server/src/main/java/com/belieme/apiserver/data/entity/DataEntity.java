package com.belieme.apiserver.data.entity;

import java.io.Serializable;

public abstract class DataEntity<T> implements Serializable {
    public abstract T getId();

    public static <T> T getIdOrElse(DataEntity<T> dataEntity, T otherValue) {
        if (dataEntity == null) {
            return otherValue;
        }
        return dataEntity.getId();
    }
}
