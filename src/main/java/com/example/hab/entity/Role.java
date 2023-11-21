package com.example.hab.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Role {
    @JsonEnumDefaultValue
    USER,
    HC
}
