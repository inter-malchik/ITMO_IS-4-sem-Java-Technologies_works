package dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class OwnerDto implements Serializable {

    private int id;

    private String name;

    private Date birthDate;

    private Set<Integer> ownerCatsIds = new HashSet<>();

    public OwnerDto(int id, @NotNull String name, @NotNull Date birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public OwnerDto(@NotNull String name, @NotNull Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public OwnerDto(String name) {
        this(name, new Date());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Integer> getOwnerCatsIds() {
        return ownerCatsIds;
    }

    public void setOwnerCats(Set<Integer> ownerCats) {
        this.ownerCatsIds = ownerCats;
    }
}
