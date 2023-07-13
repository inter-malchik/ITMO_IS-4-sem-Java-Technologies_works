package microservices;

import dao.CatDao;
import dao.OwnerDao;
import dto.KittenDto;
import dto.OwnerDto;
import entities.Kitten;
import entities.Owner;
import jakarta.annotation.Nullable;
import lombok.SneakyThrows;
import mapping.CatMapper;
import mapping.OwnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Primary
public class CatsMicroHandlerImpl implements CatsMicroHandler {
    private final CatDao catDao;

    private final OwnerDao ownerDao;

    @Autowired
    public CatsMicroHandlerImpl(CatDao catDao, OwnerDao ownerDao) {
        this.catDao = catDao;
        this.ownerDao = ownerDao;
    }

    public Kitten addCat(Kitten cat) {
        return catDao.save(cat);
    }

    @SneakyThrows
    public KittenDto createCat(String name, @Nullable Date birth_date,
                               @Nullable Integer owner_id,
                               @Nullable String species,
                               @Nullable String color) {

        if (birth_date == null) birth_date = new Date();
        else if (birth_date.after(new Date())) throw new ParseException("invalid date value", 0);
        Owner cat_owner = (owner_id == null) ? null : ownerDao.getReferenceById(owner_id);
        Kitten new_cat = new Kitten(name, birth_date, cat_owner, species, color);

        return CatMapper.toDto(addCat(new_cat));
    }

    public void addCats(List<Kitten> cats) {
        catDao.saveAll(cats);
    }

    public void deleteCat(int id) {
        catDao.deleteById(id);
    }

    public Kitten updateCat(Kitten cat) {
        return catDao.save(cat);
    }

    public KittenDto updateCatByDto(KittenDto catDto) {
        Kitten kitten = new Kitten(catDto.getId(),
                catDto.getNickname(),
                catDto.getBirth_date(),
                ownerDao.getReferenceById(catDto.getOwnerId()),
                catDto.getSpecies(),
                catDto.getColor());
        kitten.setKittyFriends(catDto.getKittyFriendsId().stream().map(id -> catDao.getReferenceById(id)).collect(Collectors.toSet()));
        return CatMapper.toDto(updateCat(kitten));
    }

    public KittenDto getCat(int id) {
        if (!existsCatById(id)) throw CatsMicroHandlerException.CannotGetCat(id);
        return CatMapper.toDto(catDao.getReferenceById(id));
    }

    public boolean existsCatById(int id) {
        return catDao.existsById(id);
    }

    public List<KittenDto> getAllCats() {
        return CatMapper.toDtosMany(catDao.findAll());
    }

    public List<KittenDto> getByFilters(@Nullable String color, @Nullable String species, @Nullable String nickname) {
        Stream<Kitten> answer = catDao.findAll().stream();

        if (color != null) {
            answer = answer.filter(kitten -> kitten.getColor().equals(color));
        }

        if (species != null) {
            answer = answer.filter(kitten -> kitten.getSpecies().equals(species));
        }

        if (nickname != null) {
            answer = answer.filter(kitten -> kitten.getNickname().equals(nickname));
        }

        return CatMapper.toDtosMany(answer.toList());
    }

    public void assignOwnerToCat(int cat_id, int owner_id) {
        Kitten cat = catDao.getReferenceById(cat_id);
        cat.setOwner(ownerDao.getReferenceById(owner_id));
        updateCat(cat);
    }

    public void assignFriendToCat(int cat_id, int friend_id) {
        Kitten cat = catDao.getReferenceById(cat_id);
        Kitten cat_friend = catDao.getReferenceById(friend_id);
        cat.getKittyFriends().add(cat_friend);
        updateCat(cat);
    }

    public OwnerDto getOwnerOfCat(int id) {
        return OwnerMapper.toDto(catDao.getReferenceById(id).getOwner());
    }

    public List<KittenDto> getFriendsOfCat(int id) {
        return CatMapper.toDtosMany(catDao.getReferenceById(id).getKittyFriends());
    }
}
