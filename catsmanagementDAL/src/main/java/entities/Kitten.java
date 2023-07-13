package entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cats", schema = "public")
public class Kitten implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birth_date")
    private Date birth_date;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Owner owner;

    @Column(name = "species")
    private String species;

    @Column(name = "color")
    private String color;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cats_friends",
            joinColumns = {@JoinColumn(name = "cat_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")}
    )
    private Set<Kitten> kittyFriends = new HashSet<Kitten>();

    public Kitten() {
    }

    public Kitten(int id, String nickname, Date birth_date, @Nullable Owner owner, @Nullable String species, @Nullable String color) {
        this.id = id;
        this.nickname = nickname;
        this.birth_date = birth_date;
        this.owner = owner;
        this.species = species;
        this.color = color;
    }

    public Kitten(String nickname, Date birth_date, @Nullable Owner owner, @Nullable String species, @Nullable String color) {
        this.nickname = nickname;
        this.birth_date = birth_date;
        this.owner = owner;
        this.species = species;
        this.color = color;
    }

    public Kitten(String nickname, @Nullable Owner owner, @Nullable String species, @Nullable String color) {
        this.nickname = nickname;
        this.birth_date = new Date();
        this.owner = owner;
        this.species = species;
        this.color = color;
    }

    public Kitten(String nickname) {
        this(nickname, new Date(), null, null, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Kitten> getKittyFriends() {
        return kittyFriends;
    }

    public void setKittyFriends(Set<Kitten> kittyFriends) {
        this.kittyFriends = kittyFriends;
    }
}
