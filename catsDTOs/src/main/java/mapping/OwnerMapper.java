package mapping;


import dto.OwnerDto;
import entities.Kitten;
import entities.Owner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OwnerMapper {
    public static OwnerDto toDto(Owner owner) {
        OwnerDto new_own = new OwnerDto(owner.getId(), owner.getName(), owner.getBirthDate());

        new_own.setOwnerCats(owner.getOwnerCats().stream().map(Kitten::getId).collect(Collectors.toSet()));
        return new_own;
    }

    public static List<OwnerDto> toDtosMany(Set<Owner> owners) {
        return owners.stream().map(OwnerMapper::toDto).toList();
    }

    public static List<OwnerDto> toDtosMany(List<Owner> owners) {
        return owners.stream().map(OwnerMapper::toDto).toList();
    }
}
