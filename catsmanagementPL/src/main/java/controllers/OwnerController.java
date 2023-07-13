package controllers;

import argobjects.OwnerArgObject;
import com.rabbitmq.client.UnblockedCallback;
import dto.KittenDto;
import dto.OwnerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import service.UserService;
import services.interfaces.OwnerService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/owners")
public class OwnerController {
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private final UserService userService;

    private final OwnerService ownerService;


    @Autowired
    public OwnerController(UserService userService, OwnerService ownerService) {
        this.userService = userService;
        this.ownerService = ownerService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping()
    public OwnerDto addOwner(@RequestBody OwnerArgObject ownerArgObject) {
        if (!ownerArgObject.isValid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid body");
        try {
            return ownerService.createOwner(ownerArgObject.name, formatter.parse(ownerArgObject.birth_date));
        } catch (ParseException exc) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error occured in parsing data format", exc);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @PutMapping()
    public OwnerDto updateOwner(@RequestBody OwnerDto owner) {
        if (!userService.isCurrentAdmin() && !userService.isCurrentOwnerById(owner.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return ownerService.updateOwnerByDto(owner);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable int id) {
        performBllCall(()->
        ownerService.deleteOwner(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping("/{id}")
    public OwnerDto getOwner(@PathVariable int id) {
        if (ownerService.getOwner(id).isPresent())
            return ownerService.getOwner(id).get();

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @RequestMapping("/get-cats/{id}")
    public List<KittenDto> getOwnerCats(@PathVariable int id) {
        return ownerService.getOwnerCats(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping()
    public List<OwnerDto> getOwners() {
        return ownerService.getAllOwners();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OWNER')")
    @RequestMapping("/assign-cat")
    public void assignCat(@RequestParam int owner_id, @RequestParam int cat_id) {
        if (userService.isCurrentOwnerById(owner_id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        performBllCall(()->ownerService.assignCatToOwner(owner_id, cat_id));
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
