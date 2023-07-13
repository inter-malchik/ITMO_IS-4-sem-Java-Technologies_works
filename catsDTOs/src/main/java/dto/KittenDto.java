package dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class KittenDto implements Serializable {

    private int id;

    private String nickname;

    private Date birth_date;

    private Integer owner_id;

    private String species;

    private String color;

    private Set<Integer> kittyFriendsId = new HashSet<>();

    public KittenDto(int id, @NotNull String nickname, @NotNull Date birth_date, @Nullable Integer owner_id, @Nullable String species, @Nullable String color) {
        this.id = id;
        this.nickname = nickname;
        this.birth_date = birth_date;
        this.owner_id = owner_id;
        this.species = species;
        this.color = color;
    }

    public KittenDto(@NotNull String nickname, @NotNull Date birth_date, @Nullable Integer owner_id, @Nullable String species, @Nullable String color) {
        this.nickname = nickname;
        this.birth_date = birth_date;
        this.owner_id = owner_id;
        this.species = species;
        this.color = color;
    }

    public KittenDto(@NotNull String nickname) {
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

    public Integer getOwnerId() {
        return owner_id;
    }

    public void setOwnerId(int owner) {
        this.owner_id = owner;
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

    public Set<Integer> getKittyFriendsId() {
        return kittyFriendsId;
    }

    public void setKittyFriends(Set<Integer> kittyFriends) {
        this.kittyFriendsId = kittyFriends;
    }
}
