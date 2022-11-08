package com.example.laboratordatabase;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class Tables implements Serializable {
    public String tableName;
    public ArrayList<String> fields = null;

    public Tables(String tableName, ArrayList<String> fields) {
        this.tableName = tableName;
        this.fields = fields;
    }

    public Tables() {
    }

    @NotNull
    @Override
    public String toString() {
        return "Tables{" +
                "tableName='" + tableName + '\'' +
                ", fields=" + fields +
                '}';
    }
}
