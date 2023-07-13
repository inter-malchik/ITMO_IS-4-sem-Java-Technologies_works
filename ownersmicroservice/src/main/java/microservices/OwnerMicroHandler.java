package microservices;

import dto.KittenDto;
import dto.OwnerDto;
import entities.Owner;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.List;

public interface OwnerMicroHandler {
    Owner addOwner(Owner owner);

    OwnerDto createOwner(String name, @Nullable Date birth_date);

    void addOwners(List<Owner> owners);

    List<KittenDto> getOwnerCats(int id);

    void deleteOwner(int id);

    OwnerDto updateOwnerByDto(OwnerDto ownerDto);

    OwnerDto getOwner(int id);

    boolean existsOwnerById(int id);

    List<OwnerDto> getAllOwners();

    void addCatToOwner(int owner_id, int cat_id);
}
