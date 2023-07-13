package dao;

import entities.Kitten;
import entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerDao extends JpaRepository<Owner, Integer> {
    List<Owner> getAllByName(String name);

    Optional<Owner> findByOwnerCatsContaining(Kitten cat);
}
