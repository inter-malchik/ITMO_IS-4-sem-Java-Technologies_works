package dao;

import entities.Kitten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatDao extends JpaRepository<Kitten, Integer> {
    List<Kitten> getAllByColor(String color);

    List<Kitten> getAllBySpecies(String species);

    List<Kitten> getAllByNickname(String nickname);

    List<Kitten> getAllByKittyFriendsContaining(Kitten friend);
}
