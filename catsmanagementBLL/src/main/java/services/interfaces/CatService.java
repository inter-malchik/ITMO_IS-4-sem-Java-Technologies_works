package services.interfaces;

import dto.KittenDto;

import java.util.Date;
import java.util.List;

public interface CatService {
    KittenDto getCat(int id);

    public KittenDto createCat(String name, Date birth_date, Integer owner_id, String species, String color);

    void deleteCat(int id);

    KittenDto updateCatByDto(KittenDto kittenDto);

    List<KittenDto> getCats(String color, String species, String nickname);

    void assignFriendToCat(int cat_id, int friend_id);

    List<KittenDto> getFriendsOfCat(int id);
}
