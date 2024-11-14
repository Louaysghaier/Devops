package tn.esprit.tpfoyer17.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer17.entities.Foyer;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FoyerRepositoryTest {

    @Autowired
    private FoyerRepository foyerRepository;

    @Test
    void saveFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer1");
        foyer.setCapaciteFoyer(200);

        Foyer savedFoyer = foyerRepository.save(foyer);
        assertNotNull(savedFoyer);
        assertEquals("Foyer1", savedFoyer.getNomFoyer());
        assertEquals(200, savedFoyer.getCapaciteFoyer());
    }

    @Test
    void findById() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer1");
        foyer.setCapaciteFoyer(200);

        Foyer savedFoyer = foyerRepository.save(foyer);
        Foyer foundFoyer = foyerRepository.findById(savedFoyer.getIdFoyer()).orElse(null);
        assertNotNull(foundFoyer);
        assertEquals("Foyer1", foundFoyer.getNomFoyer());
        assertEquals(200, foundFoyer.getCapaciteFoyer());
    }

    @Test
    void deleteFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer1");
        foyer.setCapaciteFoyer(200);

        Foyer savedFoyer = foyerRepository.save(foyer);
        foyerRepository.delete(savedFoyer);

        Foyer foundFoyer = foyerRepository.findById(savedFoyer.getIdFoyer()).orElse(null);
        assertNull(foundFoyer);
    }
}