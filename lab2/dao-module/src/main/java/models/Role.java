package models;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    USER,
    ADMIN;

    public static Optional<Role> findByValue(String name) {
        return Arrays.stream(Role.values())
                .filter(b -> b.name().equals(name))
                .findFirst();
    }
}
