package jpaentitiesmodule.jpaentitiesmodule;

import entities.Kitten;
import entities.Owner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CatDataObjectsTests {

  @Test
  void UnfilledJpaKitten_CorrectInvariant() {
    assertNull(new Kitten("puss").getColor());
    assertNull(new Kitten("puss").getOwner());
    assertNull(new Kitten("puss").getSpecies());
    assertNotNull(new Kitten("puss").getBirth_date());
    assertEquals("puss", new Kitten("puss").getNickname());
    assertEquals(0, new Kitten("puss").getKittyFriends().size());
  }

  @Test
  void UnfilledJpaOwner_CorrectInvariant() {
    assertEquals(0, new Owner("name").getOwnerCats().size());
    assertEquals("name", new Owner("name").getName());
    assertNotNull(new Owner("name").getBirthDate());
  }
}
