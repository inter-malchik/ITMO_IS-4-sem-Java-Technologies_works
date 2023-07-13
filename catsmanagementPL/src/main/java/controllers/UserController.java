package controllers;

import argobjects.UserArgObject;
import com.rabbitmq.client.UnblockedCallback;
import dto.UserDto;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import service.UserService;
import service.UserServiceException;
import services.interfaces.OwnerService;

import java.io.IOException;
import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final OwnerService ownerService;

    @Autowired
    public UserController(UserService userService, OwnerService ownerService) {
        this.userService = userService;
        this.ownerService = ownerService;
    }

    @PostMapping()
    public UserDto addUser(@RequestBody UserArgObject userArgObject) {
        if (!userArgObject.isValid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid body");
        try {
            return userService.createUser(userArgObject.username, userArgObject.password, userArgObject.isAdmin);
        } catch (UserServiceException exc) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id) {
        return userService.getById(id);
    }

    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable @NonNull String username) {
        return userService.getByUsername(username);
    }

    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @RequestBody UserArgObject userArgObject) {
        return userService.updateUser(id, userArgObject.username, userArgObject.password, userArgObject.isAdmin);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        performBllCall(() -> userService.deleteOwner(id));
    }

    @RequestMapping("/bind")
    public void bindOwnerToUser(@RequestParam String username, @RequestParam int owner_id) {
        if (ownerService.getOwner(owner_id).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }
        performBllCall(() -> userService.assignOwnerToUser(username, owner_id));
    }

    private void performBllCall(UnblockedCallback unblockedCallback) {
        try {
            unblockedCallback.handle();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
