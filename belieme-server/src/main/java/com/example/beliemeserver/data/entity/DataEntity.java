package com.example.beliemeserver.data.entity;

import java.io.Serializable;

public abstract class DataEntity implements Serializable {
    public abstract int getId();

    public static int getIdOrElse(DataEntity dataEntity, int otherValue) {
        if(dataEntity == null) {
            return otherValue;
        }
        return dataEntity.getId();
    }
}
