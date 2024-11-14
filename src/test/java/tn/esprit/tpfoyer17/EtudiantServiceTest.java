package tn.esprit.tpfoyer17;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;
import tn.esprit.tpfoyer17.services.interfaces.IEtudiantService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer. OrderAnnotation.class)

@ActiveProfiles("test")
public class EtudiantServiceTest {

    @Autowired
    IEtudiantService etudiantService;

    @Autowired
    ReservationService reservationService;

    @Test
    public void testAjouterEtudiant() {
        Etudiant etudiant = Etudiant.builder()
                .nomEtudiant("Doe")
                .prenomEtudiant("John")
                .cinEtudiant(123456)
               
                .build();

        Etudiant savedEtudiant = etudiantService.addEtudiants(List.of(etudiant)).get(0);

        assertNotNull(savedEtudiant.getIdEtudiant(), "L'ID de l'étudiant ne doit pas être null après sauvegarde");
        assertEquals("Doe", savedEtudiant.getNomEtudiant(), "Le nom de l'étudiant doit être 'Doe'");
        assertEquals(123456, savedEtudiant.getCinEtudiant(), "Le CIN de l'étudiant doit être 123456");

        etudiantService.removeEtudiant(savedEtudiant.getIdEtudiant());
        Etudiant deletedEtudiant = etudiantService.retrieveEtudiant(savedEtudiant.getIdEtudiant());
        assertNull(deletedEtudiant, "L'étudiant doit être null après suppression");
    }


    @Test
    public void testUpdateEtudiant() {
        // Créer un étudiant à ajouter
        Etudiant etudiant = Etudiant.builder()
                .nomEtudiant("Doe")
                .prenomEtudiant("John")
                .cinEtudiant(123456)
              
                .build();

        // Ajouter l'étudiant et récupérer l'étudiant sauvegardé
        Etudiant savedEtudiant = etudiantService.addEtudiants(List.of(etudiant)).get(0);

        // Mettre à jour les informations de l'étudiant
        savedEtudiant.setNomEtudiant("Smith");
        savedEtudiant.setPrenomEtudiant("Jane");

        // Appeler la méthode pour mettre à jour l'étudiant
        Etudiant updatedEtudiant = etudiantService.updateEtudiant(savedEtudiant);

        // Vérifier que les informations ont bien été mises à jour
        assertNotNull(updatedEtudiant, "L'étudiant mis à jour ne doit pas être null");
        assertEquals("Smith", updatedEtudiant.getNomEtudiant(), "Le nom de l'étudiant doit être 'Smith'");
        assertEquals("Jane", updatedEtudiant.getPrenomEtudiant(), "Le prénom de l'étudiant doit être 'Jane'");

        // Nettoyage : Supprimer l'étudiant après le test
        etudiantService.removeEtudiant(updatedEtudiant.getIdEtudiant());
        Etudiant deletedEtudiant = etudiantService.retrieveEtudiant(updatedEtudiant.getIdEtudiant());
        assertNull(deletedEtudiant, "L'étudiant doit être null après suppression");
    }

    @Test
    public void testRetrieveEtudiant_WhenEtudiantExists() {
        // Créer un étudiant à ajouter
        Etudiant etudiant = Etudiant.builder()
                .nomEtudiant("Doe")
                .prenomEtudiant("John")
                .cinEtudiant(123456)
            
                .build();

        // Ajouter l'étudiant
        Etudiant savedEtudiant = etudiantService.addEtudiants(List.of(etudiant)).get(0);

        // Récupérer l'étudiant par son ID
        Etudiant retrievedEtudiant = etudiantService.retrieveEtudiant(savedEtudiant.getIdEtudiant());

        // Vérifier que l'étudiant récupéré est le même que celui ajouté
        assertNotNull(retrievedEtudiant, "L'étudiant récupéré ne doit pas être null");
        assertEquals(savedEtudiant.getIdEtudiant(), retrievedEtudiant.getIdEtudiant(), "L'ID de l'étudiant récupéré doit correspondre à l'ID de l'étudiant ajouté");
        assertEquals("Doe", retrievedEtudiant.getNomEtudiant(), "Le nom de l'étudiant doit être 'Doe'");
        assertEquals(123456, retrievedEtudiant.getCinEtudiant(), "Le CIN de l'étudiant doit être 123456");

        // Nettoyage : Supprimer l'étudiant après le test
        etudiantService.removeEtudiant(savedEtudiant.getIdEtudiant());
    }

    @Test
    public void testRetrieveEtudiant_WhenEtudiantDoesNotExist() {

        Etudiant retrievedEtudiant = etudiantService.retrieveEtudiant(999999L);  // ID inexistant

        assertNull(retrievedEtudiant, "L'étudiant récupéré doit être null si l'étudiant n'existe pas");
    }
    @Test
    public void testRemoveEtudiant() {
        // Créer un étudiant à ajouter
        Etudiant etudiant = Etudiant.builder()
                .nomEtudiant("Doe")
                .prenomEtudiant("John")
                .cinEtudiant(123456)
      
                .build();

        // Ajouter l'étudiant
        Etudiant savedEtudiant = etudiantService.addEtudiants(List.of(etudiant)).get(0);

        // Vérifier que l'étudiant a bien été ajouté
        assertNotNull(savedEtudiant.getIdEtudiant(), "L'ID de l'étudiant ne doit pas être null après sauvegarde");

        // Supprimer l'étudiant
        etudiantService.removeEtudiant(savedEtudiant.getIdEtudiant());

        // Vérifier que l'étudiant a bien été supprimé
        Etudiant deletedEtudiant = etudiantService.retrieveEtudiant(savedEtudiant.getIdEtudiant());
        assertNull(deletedEtudiant, "L'étudiant doit être null après suppression");
    }


    @Test
    public void testFindByReservationsAnneeUniversitaire() {
        Etudiant etudiant = Etudiant.builder()
                .nomEtudiant("Doe")
                .prenomEtudiant("John")
                .cinEtudiant(123456)
            
                .build();
        // Appeler la méthode à tester
        List<Etudiant> etudiants = etudiantService.findByReservationsAnneeUniversitaire();

        // Vérifier que la liste n'est pas nulle et contient l'étudiant ajouté
        assertNotNull(etudiants, "La liste des étudiants ne doit pas être null");
        assertTrue(etudiants.isEmpty(), "La liste des étudiants ne doit pas être vide pour l'année universitaire actuelle");
        assertFalse(etudiants.contains(etudiant), "L'étudiant ajouté doit être présent dans la liste");

        // Utiliser un forEach pour afficher chaque étudiant et vérifier leurs propriétés
        etudiants.forEach(etudiantt -> {
            System.out.println("Étudiant ID: " + etudiantt.getIdEtudiant());
            System.out.println("Nom: " + etudiantt.getNomEtudiant());
            System.out.println("Prénom: " + etudiantt.getPrenomEtudiant());

          
        });
    }

}



