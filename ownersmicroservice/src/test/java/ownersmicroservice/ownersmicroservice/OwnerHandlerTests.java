package ownersmicroservice.ownersmicroservice;

import dao.CatDao;
import dao.OwnerDao;
import entities.Kitten;
import entities.Owner;
import microservices.OwnerMicroHandlerException;
import microservices.OwnerMicroHandlerImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OwnerHandlerTests {
  @Test
  void GetUnexistantEntitiesFromService_ThrowException() {
    CatDao d_k = mock(CatDao.class);
    when(d_k.getReferenceById(13)).thenReturn(null);
    when(d_k.getReferenceById(24)).thenReturn(new Kitten("meow"));

    when(d_k.existsById(13)).thenReturn(false);
    when(d_k.existsById(24)).thenReturn(true);

    OwnerDao d_o = mock(OwnerDao.class);
    when(d_o.getReferenceById(28)).thenReturn(null);
    when(d_o.getReferenceById(16)).thenReturn(new Owner("gaw"));

    when(d_o.existsById(28)).thenReturn(false);
    when(d_o.existsById(16)).thenReturn(true);

    OwnerMicroHandlerImpl os = new OwnerMicroHandlerImpl(d_k, d_o);

    assertThrows(OwnerMicroHandlerException.class, () -> {
      os.getOwner(28);
    });
    assertDoesNotThrow(() -> {
      os.getOwner(16);
    });
  }
}
