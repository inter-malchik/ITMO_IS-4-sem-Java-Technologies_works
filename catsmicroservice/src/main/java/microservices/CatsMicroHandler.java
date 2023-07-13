package microservices;

import dto.KittenDto;
import dto.OwnerDto;
import entities.Kitten;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.List;

public interface CatsMicroHandler {
    Kitten addCat(Kitten cat);

    KittenDto createCat(String name, @Nullable Date birth_date,
                        @Nullable Integer owner_id,
                        @Nullable String species,
                        @Nullable String color);

    void addCats(List<Kitten> cats);

    void deleteCat(int id);

    KittenDto updateCatByDto(KittenDto catDto);

    KittenDto getCat(int id);

    boolean existsCatById(int id);

    List<KittenDto> getAllCats();

    List<KittenDto> getByFilters(@Nullable String color, @Nullable String species, @Nullable String nickname);

    void assignOwnerToCat(int cat_id, int owner_id);

    void assignFriendToCat(int cat_id, int friend_id);

    OwnerDto getOwnerOfCat(int id);

    List<KittenDto> getFriendsOfCat(int id);
}
