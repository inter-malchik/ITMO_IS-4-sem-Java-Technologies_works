package service;

import dto.UserDto;
import lombok.NonNull;

import java.util.List;

public interface UserService {
    UserDto createUser(@NonNull String username, @NonNull String password, boolean isAdmin);

    UserDto updateUser(int id, String username, String password, Boolean isAdmin);

    UserDto getCurrentUser();

    UserDto getById(int id);

    UserDto getByUsername(String username);

    List<UserDto> getAllUsers();

    void deleteOwner(int id);

    void assignOwnerToUser(String username, int owner_id);

    boolean isCurrentAdmin();

    boolean isCurrentOwnerById(int owner_id);

    boolean existsByUsername(String username);
}
