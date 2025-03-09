package com.example.logisticstracking.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PackageStatus {
    CREATED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED,
    UNKNOWN;

    @JsonCreator
    public static PackageStatus fromString(String value) {
        try {
            return PackageStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
