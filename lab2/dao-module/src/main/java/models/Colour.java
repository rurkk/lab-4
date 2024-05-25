package models;

import java.util.Arrays;

public enum Colour {
    White,
    Black,
    Grey,
    Fawn,
    Unknown;

    public static Colour findByValue(String name) {
        return Arrays.stream(Colour.values())
                .filter(b -> b.name().equals(name))
                .findFirst()
                .orElse(Unknown);
    }

}
