package service;

import dao.OwnerDao;
import daos.User;
import daos.UserDao;
import dto.UserDto;
import lombok.NonNull;
import mapping.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    private final OwnerDao ownerDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, OwnerDao ownerDao) {
        this.userDao = userDao;
        this.ownerDao = ownerDao;
    }

    public UserDto createUser(@NonNull String username, @NonNull String password, boolean isAdmin) {
        User user = new User();
        if (existsByUsername(username)) {
            throw UserServiceException.UserAlreadyPresented(username);
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setAdmin(isAdmin);

        return UserMapper.toDto(userDao.save(user));
    }

    public User updateUser(User user) {
        return userDao.save(user);
    }

    public UserDto updateUser(int id, String username, String password, Boolean isAdmin) {
        User updatedUser = userDao.getReferenceById(id);
        updatedUser.setAdmin(isAdmin);
        if (existsByUsername(username)) {
            throw UserServiceException.UserAlreadyPresented(username);
        }
        updatedUser.setUsername(username);
        updatedUser.setPassword(password);
        return UserMapper.toDto(updateUser(updatedUser));
    }


    public UserDto getCurrentUser() {
        Optional<User> currentUser = userDao.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return UserMapper.toDto(currentUser.get());
    }

    public UserDto getById(int id) {
        return UserMapper.toDto(userDao.getReferenceById(id));
    }

    public UserDto getByUsername(String username) {
        return UserMapper.toDto(userDao.getByUsername(username).get());
    }

    public List<UserDto> getAllUsers() {
        return UserMapper.toDtosMany(userDao.findAll());
    }

    public void deleteOwner(int id) {
        userDao.deleteById(id);
    }

    public void assignOwnerToUser(String username, int owner_id) {
        User currentUser = userDao.getByUsername(username).get();
        currentUser.setOwner(ownerDao.getReferenceById(owner_id));
        userDao.save(currentUser);
    }

    public boolean isCurrentAdmin() {
        return getCurrentUser().isAdmin();
    }

    public boolean isCurrentOwnerById(int owner_id) {
        return getCurrentUser().getOwner_id() == owner_id;
    }

    public boolean existsByUsername(String username) {
        return userDao.getByUsername(username).isPresent();
    }
}
