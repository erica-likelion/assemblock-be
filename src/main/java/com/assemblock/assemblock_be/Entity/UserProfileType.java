package com.assemblock.assemblock_be.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserProfileType {
    TYPE_1("Type_1"),
    TYPE_2("Type_2"),
    TYPE_3("Type_3"),
    TYPE_4("Type_4"),
    TYPE_5("Type_5");

    private final String value;

    UserProfileType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserProfileType fromValue(String value) {
        for (UserProfileType type : UserProfileType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown UserProfileType: " + value);
    }
}