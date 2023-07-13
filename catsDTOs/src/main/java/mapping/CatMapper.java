package mapping;

import dto.KittenDto;
import entities.Kitten;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CatMapper {
    public static KittenDto toDto(Kitten kitten) {
        KittenDto new_cat = new KittenDto(kitten.getId(),
                kitten.getNickname(),
                kitten.getBirth_date(),
                (kitten.getOwner() != null) ? kitten.getOwner().getId() : null,
                kitten.getSpecies(),
                kitten.getColor());

        new_cat.setKittyFriends(kitten.getKittyFriends().stream().map(Kitten::getId).collect(Collectors.toSet()));

        return new_cat;
    }

    public static List<KittenDto> toDtosMany(Set<Kitten> kittens) {
        return kittens.stream().map(CatMapper::toDto).toList();
    }

    public static List<KittenDto> toDtosMany(List<Kitten> kittens) {
        return kittens.stream().map(CatMapper::toDto).toList();
    }
}
