package com.example.awkow2x.myapplication1;

import io.realm.RealmObject;

public class RealmData extends RealmObject {
    private String id;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
