package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.services.interfaces.IChambreService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChambreServiceIntegrationTest {

    @Autowired
    private IChambreService chambreService;

    @Autowired
    private BlocRepository blocRepository;

    private static long savedChambreId;
    private static long savedBlocId;

    @Test
    @Order(1)
    public void testAddChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambre.setTypeChambre(TypeChambre.SIMPLE);

        // Act
        Chambre savedChambre = chambreService.addChambre(chambre);
        savedChambreId = savedChambre.getIdChambre();

        // Assert
        assertNotNull(savedChambre);
        assertEquals(101L, savedChambre.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, savedChambre.getTypeChambre());
    }

    @Test
    @Order(2)
    public void testRetrieveChambre() {
        // Act
        Chambre retrievedChambre = chambreService.retrieveChambre(savedChambreId);

        // Assert
        assertNotNull(retrievedChambre);
        assertEquals(savedChambreId, retrievedChambre.getIdChambre());
    }

    @Test
    @Order(3)
    public void testUpdateChambre() {
        // Arrange
        Chambre chambreToUpdate = chambreService.retrieveChambre(savedChambreId);
        chambreToUpdate.setTypeChambre(TypeChambre.DOUBLE);

        // Act
        Chambre updatedChambre = chambreService.updateChambre(chambreToUpdate);

        // Assert
        assertEquals(TypeChambre.DOUBLE, updatedChambre.getTypeChambre());
    }

    @Test
    @Order(4)
    public void testRetrieveAllChambres() {
        // Arrange
        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(102L);
        chambre1.setTypeChambre(TypeChambre.DOUBLE);
        chambreService.addChambre(chambre1);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(103L);
        chambre2.setTypeChambre(TypeChambre.TRIPLE);
        chambreService.addChambre(chambre2);

        // Act
        List<Chambre> chambres = chambreService.retrieveAllChambres();

        // Assert
        assertTrue(chambres.size() >= 3); // Adjust based on initial data
    }

    @Test
    @Order(5)
    public void testFindByTypeChambre() {
        // Act
        List<Chambre> chambres = chambreService.findByTypeChambre();

        // Assert
        assertFalse(chambres.isEmpty());
        for (Chambre c : chambres) {
            assertEquals(TypeChambre.DOUBLE, c.getTypeChambre());
        }
    }



   
}
