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

@Service
@Primary
public class OwnerMicroHandlerImpl implements OwnerMicroHandler {
    private final OwnerDao ownerDao;

    private final CatDao catDao;

    @Autowired
    public OwnerMicroHandlerImpl(CatDao catDao, OwnerDao ownerDao) {
        this.catDao = catDao;
        this.ownerDao = ownerDao;
    }

    public Owner addOwner(Owner owner) {
        return ownerDao.save(owner);
    }

    @SneakyThrows
    public OwnerDto createOwner(String name, @Nullable Date birth_date) {
        Owner new_owner;
        if (birth_date == null) {
            new_owner = new Owner(name);
        } else if (birth_date.before(new Date()) || birth_date.equals(new Date())) {
            new_owner = new Owner(name, birth_date);
        } else {
            throw new ParseException("invalid date value", 0);
        }

        return OwnerMapper.toDto(addOwner(new_owner));
    }

    public void addOwners(List<Owner> owners) {
        ownerDao.saveAll(owners);
    }

    public List<KittenDto> getOwnerCats(int id) {
        return CatMapper.toDtosMany(ownerDao.getReferenceById(id).getOwnerCats());
    }

    public void deleteOwner(int id) {
        ownerDao.deleteById(id);
    }

    public Owner updateOwner(Owner owner) {
        return ownerDao.save(owner);
    }

    public OwnerDto updateOwnerByDto(OwnerDto ownerDto) {
        Owner owner = new Owner(ownerDto.getId(), ownerDto.getName(), ownerDto.getBirthDate());
        owner.setOwnerCats(ownerDto.getOwnerCatsIds().stream().map(id -> catDao.getReferenceById(id)).collect(Collectors.toSet()));
        return OwnerMapper.toDto(updateOwner(owner));
    }

    public OwnerDto getOwner(int id) {
        if (!existsOwnerById(id)) throw OwnerMicroHandlerException.CannotGetOwner(id);
        return OwnerMapper.toDto(ownerDao.getReferenceById(id));
    }

    public boolean existsOwnerById(int id) {
        return ownerDao.existsById(id);
    }

    public List<OwnerDto> getAllOwners() {
        return OwnerMapper.toDtosMany(ownerDao.findAll());
    }

    public void addCatToOwner(int owner_id, int cat_id) {
        Kitten cat = catDao.getReferenceById(cat_id);
        Owner owner = ownerDao.getReferenceById(owner_id);
        cat.setOwner(owner);
        catDao.save(cat);
    }
}
