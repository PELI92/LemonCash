package com.example.demo.lemoncash.exchange.movement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

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

    @JsonCreator
    public static MovementType decode(final String code) {
        return Stream.of(MovementType.values())
                .filter(e -> StringUtils.equalsIgnoreCase(code, e.getName()))
                .findFirst()
                .orElse(null);
    }

}
