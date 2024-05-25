package models;

import java.util.Arrays;

public enum Breed {
    Munchkin,
    Oriental,
    Persian,
    Ragdoll,
    Sphynx,
    Unknown;

    public static Breed findByValue(String name) {
        return Arrays.stream(Breed.values())
                .filter(b -> b.name().equals(name))
                .findFirst()
                .orElse(Unknown);
    }
}
