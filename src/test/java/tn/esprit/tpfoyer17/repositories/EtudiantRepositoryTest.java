package tn.esprit.tpfoyer17.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer17.entities.Etudiant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EtudiantRepositoryTest {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Test
    void findByCinEtudiant() {
        // Create an Etudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(12345678L);
        etudiant.setNomEtudiant("John");
        etudiant.setPrenomEtudiant("Doe");

        // Save the Etudiant
        etudiantRepository.save(etudiant);

        // Retrieve the Etudiant by CIN
        Etudiant foundEtudiant = etudiantRepository.findByCinEtudiant(12345678L);
        assertNotNull(foundEtudiant);
        assertEquals("John", foundEtudiant.getNomEtudiant());
        assertEquals("Doe", foundEtudiant.getPrenomEtudiant());
    }
}