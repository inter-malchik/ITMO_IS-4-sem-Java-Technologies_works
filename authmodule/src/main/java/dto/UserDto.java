package dto;

import lombok.Data;

@Data
public class UserDto {

    private int id;

    private String username;

    private boolean isAdmin;

    private Integer owner_id;

    public UserDto(int id, String username, boolean isAdmin, Integer owner_id) {
        this.id = id;
        this.username = username;
        this.isAdmin = isAdmin;
        this.owner_id = owner_id;
    }
}
