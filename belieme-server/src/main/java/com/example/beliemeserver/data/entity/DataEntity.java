package com.example.beliemeserver.data.entity;

import java.io.Serializable;

public abstract class DataEntity implements Serializable {
    public abstract int getId();

    public static Integer getIdOrElse(DataEntity dataEntity, Integer otherValue) {
        if(dataEntity == null) {
            return otherValue;
        }
        return dataEntity.getId();
    }
}
