package controllers;

import argobjects.KittenArgObject;
import com.rabbitmq.client.UnblockedCallback;
import dto.KittenDto;
import dto.OwnerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import service.UserService;
import services.interfaces.CatService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping("/cats")
public class CatsController {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private final UserService userService;

    private final OwnerController ownerController;

    private final CatService catService;

    @Autowired
    public CatsController(UserService userService, OwnerController ownerController, CatService catService) {
        this.userService = userService;
        this.catService = catService;
        this.ownerController = ownerController;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @PostMapping()
    public KittenDto addCat(@RequestBody KittenArgObject kittenArgObject) {
        if (!kittenArgObject.isValid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid body");

        if (!userService.isCurrentAdmin() && !userService.isCurrentOwnerById(kittenArgObject.owner_id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        try {
            return catService.createCat(kittenArgObject.name, formatter.parse(kittenArgObject.birth_date), kittenArgObject.owner_id, kittenArgObject.species, kittenArgObject.color);

        } catch (ParseException exc) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error occured in parsing data format", exc);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @PutMapping()
    public KittenDto updateCat(@RequestBody KittenDto cat) {
        if (!userService.isCurrentAdmin() && !userService.isCurrentOwnerById(cat.getOwnerId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return catService.updateCatByDto(cat);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCat(@PathVariable int id) {
        performBllCall(() -> catService.deleteCat(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping("/{id}")
    public KittenDto getCat(@PathVariable int id) {
        KittenDto catToFind = catService.getCat(id);

        if (!userService.isCurrentAdmin() &&
                !Objects.equals(catToFind.getOwnerId(), userService.getCurrentUser().getOwner_id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return catToFind;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/get-owner/{id}")
    public OwnerDto getOwnerOfCat(@PathVariable int id) {
        return ownerController.getOwners().stream().filter(ownerDto -> ownerDto.getId() == id).findFirst().get();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @RequestMapping("/get-friends/{id}")
    public List<KittenDto> getCatFriends(@PathVariable int id) {
        if (!userService.isCurrentAdmin() &&
                getOwnerOfCat(id).getId() != userService.getCurrentUser().getOwner_id()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return catService.getFriendsOfCat(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping()
    public List<KittenDto> getCats(@RequestParam(required = false) String color,
                                   @RequestParam(required = false) String species,
                                   @RequestParam(required = false) String nickname) {


        if (userService.isCurrentAdmin()) {
            return catService.getCats(color, species, nickname);
        } else {
            return catService.getCats(color, species, nickname).stream().filter(
                    catDto -> Objects.equals(catDto.getOwnerId(), userService.getCurrentUser().getOwner_id())).toList();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/assign-owner")
    public void assignOwner(@RequestParam int cat_id, @RequestParam int owner_id) {
        performBllCall(() -> ownerController.assignCat(owner_id, cat_id));
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @RequestMapping("/assign-friend")
    public void assignFriend(@RequestParam int cat_id, @RequestParam int friend_id) {
        if (!userService.isCurrentAdmin() &&
                getOwnerOfCat(cat_id).getId() != userService.getCurrentUser().getOwner_id()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        performBllCall(() -> catService.assignFriendToCat(cat_id, friend_id));
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
