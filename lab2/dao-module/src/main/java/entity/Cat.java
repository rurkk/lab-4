package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.Breed;
import models.Colour;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cats")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthdate")
    private LocalDate birthDate;

    @Column(name = "breed")
    private Breed breed;

    @Column(name = "colour")
    private Colour colour;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner")
    private Owner owner;

    @ManyToMany(cascade = {
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    private Set<Cat> friends;

    public Cat(String name, LocalDate birthDate, Breed breed, Colour colour, Owner owner, Set<Cat> friends) {
        this.name = name;
        this.birthDate = birthDate;
        this.breed = breed;
        this.colour = colour;
        this.owner = owner;
        this.friends = friends;
    }

    public void addFriend(Cat friend) {
        Objects.requireNonNull(friend);
        friends.add(friend);
    }

    public void unfriend(Cat exFriend) {
        Objects.requireNonNull(exFriend);
        friends = friends.stream()
                .filter(c -> !c.equals(exFriend))
                .collect(Collectors.toSet());
    }


    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
