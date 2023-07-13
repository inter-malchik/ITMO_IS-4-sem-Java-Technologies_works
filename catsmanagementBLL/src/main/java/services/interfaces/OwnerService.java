package services.interfaces;

import dto.KittenDto;
import dto.OwnerDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OwnerService {
    Optional<OwnerDto> getOwner(int id);

    void assignCatToOwner(int owner_id, int cat_id);

    List<OwnerDto> getAllOwners();

    void deleteOwner(int id);

    OwnerDto updateOwnerByDto(OwnerDto ownerDto);

    OwnerDto createOwner(String name, Date birth_date);

    List<KittenDto> getOwnerCats(int id);
}
