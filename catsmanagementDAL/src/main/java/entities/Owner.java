package entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owners", schema = "public")
public class Owner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private Date birthDate;

    @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Kitten> ownerCats = new HashSet<Kitten>();

    public Owner() {
    }

    public Owner(int id, String name, Date birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public Owner(String name, Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Owner(String name) {
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

    public Set<Kitten> getOwnerCats() {
        return ownerCats;
    }

    public void setOwnerCats(Set<Kitten> ownerCats) {
        this.ownerCats = ownerCats;
    }
}
