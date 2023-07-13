package daos;

import entities.Owner;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "users", schema = "public")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @OneToOne(optional = true)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    public User() {
    }

    public User(int id, String username, String password, boolean enabled, boolean isAdmin, Owner owner) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.isAdmin = isAdmin;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
