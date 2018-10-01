package com.kafka.consumerClean;

public class User {

    private String name;
    private String birthday;
    private String value;

    public User(String name, String birthday, String value) {
        super();
        this.name = name;
        this.birthday = birthday;
        this.value = value;
    }

    public User() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
