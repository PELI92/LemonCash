package com.example.demo.lemoncash.config;

public enum Status {
    INACTIVE(0),
    ACTIVE(1),
    SUSPENDED(2);

    private Integer value;

    Status(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
