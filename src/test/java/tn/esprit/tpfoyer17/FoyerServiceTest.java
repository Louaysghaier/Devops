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
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.services.impementations.FoyerService;
import tn.esprit.tpfoyer17.services.interfaces.IBlocService;
import tn.esprit.tpfoyer17.services.interfaces.IFoyerService;
import tn.esprit.tpfoyer17.services.interfaces.IUniversiteService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired
    FoyerRepository foyerRepository ;

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

        // Create a Foyer with blocs
        Set<Bloc> blocs = new HashSet<>();
        blocs.add(Bloc.builder().nomBloc("Bloc 1").capaciteBloc(100).build());
        blocs.add(Bloc.builder().nomBloc("Bloc 2").capaciteBloc(150).build());

        Foyer foyer = Foyer.builder().nomFoyer("Foyer G").blocs(blocs).build();

        // Add the Foyer and link it to the Universite
        // Add the Foyer and link it to the Universite
        Foyer savedFoyer = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, universite.getIdUniversite());

// Reload the savedFoyer to get the latest state from the database
        savedFoyer = foyerRepository.findById(savedFoyer.getIdFoyer()).orElse(null);

// Now perform the assertions
        Assertions.assertNotNull(savedFoyer);
        Assertions.assertNotNull(savedFoyer.getUniversite(), "The Foyer should be associated with a Universite");


        // Also verify that the Universite now has the correct Foyer associated
        Universite updatedUniversite = universiteService.retrieveUniversity(universite.getIdUniversite());
        Assertions.assertNotNull(updatedUniversite.getFoyer(), "The Universite should now have a Foyer associated with it");
        Assertions.assertEquals(savedFoyer.getIdFoyer(), updatedUniversite.getFoyer().getIdFoyer(),
                "The Universite should be associated with the correct Foyer");

        // Cleanup
       foyerService.removeFoyer(savedFoyer.getIdFoyer());
       // universiteService.desaffecterFoyerAUniversite(universite.getIdUniversite());
    }


/*
    @Test
    public void testDeleteAll() {
        // Créer plusieurs foyers pour tester la suppression
        Foyer foyer1 = Foyer.builder().nomFoyer("Foyer A").build();
        Foyer foyer2 = Foyer.builder().nomFoyer("Foyer B").build();
        foyerService.addFoyer(foyer1);
        foyerService.addFoyer(foyer2);

        // Vérifier qu'ils existent avant la suppression
        List<Foyer> foyersAvantSuppression = foyerService.retrieveAllFoyers();
        Assertions.assertEquals(3, foyersAvantSuppression.size(), "Il doit y avoir 2 foyers avant la suppression");

        // Supprimer tous les foyers
        foyerService.deleteAll();

        // Vérifier qu'il n'y a plus de foyers après la suppression
        List<Foyer> foyersApresSuppression = foyerService.retrieveAllFoyers();
        Assertions.assertTrue(foyersApresSuppression.isEmpty(), "La liste des foyers doit être vide après la suppression");
    }*/
}


