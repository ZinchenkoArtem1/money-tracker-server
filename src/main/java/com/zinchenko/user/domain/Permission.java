package com.zinchenko.user.domain;

public enum Permission {
    USER_ALL("user:all"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");

    private final String name;

    Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
