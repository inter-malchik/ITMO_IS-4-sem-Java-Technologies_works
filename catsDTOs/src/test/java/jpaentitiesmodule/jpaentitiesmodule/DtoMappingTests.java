package jpaentitiesmodule.jpaentitiesmodule;

import dto.KittenDto;
import dto.OwnerDto;
import entities.Kitten;
import entities.Owner;
import mapping.CatMapper;
import mapping.OwnerMapper;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DtoMappingTests {

  @Test
  void EmptyKitten_CorrectCast() {
    Kitten kitten = new Kitten("cat", new Date(), null, null, null);
    KittenDto kittenDto = CatMapper.toDto(kitten);

    assertNotNull(kittenDto.getNickname());
    assertEquals(0, kittenDto.getKittyFriendsId().size());
    assertNull(kittenDto.getOwnerId());
    assertNull(kittenDto.getSpecies());
    assertNull(kittenDto.getColor());
  }

  @Test
  void EmptyOwner_CorrectCast() {
    Owner owner = new Owner("own");
    OwnerDto ownerDto = OwnerMapper.toDto(owner);

    assertNotNull(ownerDto.getName());
    assertNotNull(ownerDto.getBirthDate());
    assertEquals(0, ownerDto.getOwnerCatsIds().size());
  }
}
