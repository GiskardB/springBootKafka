package com.kafka.consumerDirty;

public class User {

    private String id;
    private String value;

    public User(String id, String value) {
        super();
        this.id = id;
        this.value = value;
    }

    public User() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
