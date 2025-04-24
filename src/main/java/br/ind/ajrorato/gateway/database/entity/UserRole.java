package br.ind.ajrorato.gateway.database.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("S"),
    USER("N");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public static UserRole fromValue(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Valor inválido para UserRole: " + value);
    }
}
