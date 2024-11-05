package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.services.impementations.FoyerService;
import tn.esprit.tpfoyer17.services.interfaces.IBlocService;
import tn.esprit.tpfoyer17.services.interfaces.IFoyerService;
import tn.esprit.tpfoyer17.services.interfaces.IUniversiteService;

import java.util.List;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles("test")
public class FoyerServiceTest {

    @Autowired
    IFoyerService foyerService;
    @Autowired
    IUniversiteService universiteService;
    @Autowired
    IBlocService blocService;

    // Test for addFoyer()
    @Test
    public void testAddFoyer() {
        Foyer foyer = Foyer.builder().nomFoyer("Foyer A").build();
        Foyer savedFoyer = foyerService.addFoyer(foyer);

        Assertions.assertNotNull(savedFoyer.getIdFoyer(), "L'ID du Foyer ne doit pas être null après sauvegarde");
        Assertions.assertEquals("Foyer A", savedFoyer.getNomFoyer(), "Le nom du Foyer doit être 'Foyer A'");

        foyerService.removeFoyer(savedFoyer.getIdFoyer());
        Foyer deletedFoyer = foyerService.retrieveFoyer(savedFoyer.getIdFoyer());
        Assertions.assertNull(deletedFoyer, "Le Foyer doit être null après suppression");
    }

    // Test for retrieveAllFoyers()
    @Test
    public void testRetrieveAllFoyers() {
        Foyer foyer1 = Foyer.builder().nomFoyer("Foyer B").build();
        Foyer foyer2 = Foyer.builder().nomFoyer("Foyer C").build();
        foyerService.addFoyer(foyer1);
        foyerService.addFoyer(foyer2);

        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        Assertions.assertFalse(foyers.isEmpty(), "La liste des foyers ne doit pas être vide");
        Assertions.assertEquals(2, foyers.size(), "Il doit y avoir 2 foyers récupérés");

        // Nettoyage
        foyerService.removeFoyer(foyer1.getIdFoyer());
        foyerService.removeFoyer(foyer2.getIdFoyer());
    }

    // Test for updateFoyer()
    @Test
    public void testUpdateFoyer() {
        Foyer foyer = Foyer.builder().nomFoyer("Foyer D").build();
        Foyer savedFoyer = foyerService.addFoyer(foyer);

        savedFoyer.setNomFoyer("Foyer D Updated");
        Foyer updatedFoyer = foyerService.updateFoyer(savedFoyer);

        Assertions.assertEquals("Foyer D Updated", updatedFoyer.getNomFoyer(), "Le nom du Foyer doit être mis à jour");

        foyerService.removeFoyer(savedFoyer.getIdFoyer());
    }

    // Test for retrieveFoyer(long idFoyer)
    @Test
    public void testRetrieveFoyer() {
        Foyer foyer = Foyer.builder().nomFoyer("Foyer E").build();
        Foyer savedFoyer = foyerService.addFoyer(foyer);

        Foyer retrievedFoyer = foyerService.retrieveFoyer(savedFoyer.getIdFoyer());
        Assertions.assertNotNull(retrievedFoyer, "Le Foyer récupéré ne doit pas être null");
        Assertions.assertEquals(savedFoyer.getIdFoyer(), retrievedFoyer.getIdFoyer(), "L'ID du Foyer récupéré doit correspondre à celui du Foyer ajouté");
        Assertions.assertEquals("Foyer E", retrievedFoyer.getNomFoyer(), "Le nom du Foyer doit être 'Foyer E'");

        foyerService.removeFoyer(savedFoyer.getIdFoyer());
    }

    // Test for removeFoyer(long idFoyer)
    @Test
    public void testRemoveFoyer() {
        Foyer foyer = Foyer.builder().nomFoyer("Foyer F").build();
        Foyer savedFoyer = foyerService.addFoyer(foyer);

        foyerService.removeFoyer(savedFoyer.getIdFoyer());
        Foyer deletedFoyer = foyerService.retrieveFoyer(savedFoyer.getIdFoyer());
        Assertions.assertNull(deletedFoyer, "Le Foyer doit être null après suppression");
    }

    // Test for ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite)
    @Test
    public void testAjouterFoyerEtAffecterAUniversite() {
        Universite universite = Universite.builder().nomUniversite("Université A").build();
        universiteService.addUniversity(universite);

        Foyer foyer = Foyer.builder().nomFoyer("Foyer G").build();
        Foyer savedFoyer = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, universite.getIdUniversite());

        Assertions.assertNotNull(savedFoyer.getIdFoyer(), "L'ID du Foyer ne doit pas être null après sauvegarde");
        Assertions.assertEquals(universite.getIdUniversite(), savedFoyer.getUniversite().getIdUniversite(), "Le Foyer doit être associé à l'Université correcte");

        // Nettoyage
        foyerService.removeFoyer(savedFoyer.getIdFoyer());
        universiteService.retrieveUniversity(universite.getIdUniversite());
    }
}
