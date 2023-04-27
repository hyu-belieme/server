package com.example.beliemeserver.data.entity._new;

import java.io.Serializable;

public abstract class NewDataEntity<T> implements Serializable {
    public abstract T getId();

    public static <T> T getIdOrElse(NewDataEntity<T> dataEntity, T otherValue) {
        if (dataEntity == null) {
            return otherValue;
        }
        return dataEntity.getId();
    }
}
