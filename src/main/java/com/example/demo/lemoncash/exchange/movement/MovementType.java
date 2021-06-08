package com.example.demo.lemoncash.exchange.movement;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MovementType {
    DEPOSIT("deposit"),
    EXTRACT("extract"),
    TRANSFER("transfer");

    private String name;

    MovementType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
