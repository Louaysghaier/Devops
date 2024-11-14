package tn.esprit.tpfoyer17.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer17.entities.Universite;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UniversiteRepositoryTest {

    @Autowired
    private UniversiteRepository universiteRepository;

    @Test
    void findByNomUniversite() {
        // Create a Universite
        Universite universite = new Universite();
        universite.setNomUniversite("University1");
        universite.setAdresse("123 Main St");

        // Save the Universite
        universiteRepository.save(universite);

        // Retrieve the Universite by name
        Universite foundUniversite = universiteRepository.findByNomUniversiteLike("University1");
        assertNotNull(foundUniversite);
        assertEquals("University1", foundUniversite.getNomUniversite());
        assertEquals("123 Main St", foundUniversite.getAdresse());
    }
}