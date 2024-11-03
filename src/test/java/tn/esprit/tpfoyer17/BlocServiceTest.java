package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.services.interfaces.IBlocService;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class BlocServiceTest {


    @Autowired
    IBlocService blocService;

    // Test for addBloc()
    @Test
    public void testAjouterBloc() {
        Bloc bloc = Bloc.builder().nomBloc("Bloc A").capaciteBloc(100).build();
        Bloc savedBloc = blocService.addBloc(bloc);

        Assertions.assertNotNull(savedBloc.getIdBloc(), "L'ID du Bloc ne doit pas être null après sauvegarde");
        Assertions.assertTrue(savedBloc.getNomBloc().length() < 20, "Le nom du Bloc doit contenir moins de 20 caractères");
        Assertions.assertEquals(100, savedBloc.getCapaciteBloc(), "La capacité du bloc doit être égale à 100");

        blocService.removeBloc(savedBloc.getIdBloc());
        Bloc deletedBloc = blocService.retrieveBloc(savedBloc.getIdBloc());
        Assertions.assertNull(deletedBloc, "Le Bloc doit être null après suppression");
    }

    // Test for retrieveBlocs()
    @Test
    public void testRetrieveBlocs() {
        Bloc bloc1 = Bloc.builder().nomBloc("Bloc B1").capaciteBloc(50).build();
        Bloc bloc2 = Bloc.builder().nomBloc("Bloc B2").capaciteBloc(75).build();
        blocService.addBloc(bloc1);
        blocService.addBloc(bloc2);

        List<Bloc> blocs = blocService.retrieveBlocs();
        Assertions.assertFalse(blocs.isEmpty(), "La liste des blocs ne doit pas être vide");
        Assertions.assertEquals(2, blocs.size(), "Il doit y avoir 2 blocs récupérés");

        // Nettoyage
        blocService.removeBloc(bloc1.getIdBloc());
        blocService.removeBloc(bloc2.getIdBloc());
    }

    // Test for updateBloc()
    @Test
    public void testUpdateBloc() {
        Bloc bloc = Bloc.builder().nomBloc("Bloc C").capaciteBloc(120).build();
        Bloc savedBloc = blocService.addBloc(bloc);

        savedBloc.setCapaciteBloc(200);
        Bloc updatedBloc = blocService.updateBloc(savedBloc);

        Assertions.assertEquals(200, updatedBloc.getCapaciteBloc(), "La capacité du Bloc doit être mise à jour");

        blocService.removeBloc(savedBloc.getIdBloc());
    }

    // Test for retrieveBloc(long idBloc)
    @Test
    public void testRetrieveBloc() {
        Bloc bloc = Bloc.builder().nomBloc("Bloc D").capaciteBloc(150).build();
        Bloc savedBloc = blocService.addBloc(bloc);

        Bloc retrievedBloc = blocService.retrieveBloc(savedBloc.getIdBloc());
        Assertions.assertNotNull(retrievedBloc, "Le Bloc récupéré ne doit pas être null");
        Assertions.assertEquals(savedBloc.getIdBloc(), retrievedBloc.getIdBloc(), "L'ID du Bloc récupéré doit correspondre à celui du Bloc ajouté");
        Assertions.assertEquals(150, retrievedBloc.getCapaciteBloc(), "La capacité du Bloc doit être égale à 150");

        blocService.removeBloc(savedBloc.getIdBloc());
    }

    // Test for removeBloc(long idBloc)
    @Test
    public void testRemoveBloc() {
        Bloc bloc = Bloc.builder().nomBloc("Bloc E").capaciteBloc(200).build();
        Bloc savedBloc = blocService.addBloc(bloc);

        blocService.removeBloc(savedBloc.getIdBloc());
        Bloc deletedBloc = blocService.retrieveBloc(savedBloc.getIdBloc());
        Assertions.assertNull(deletedBloc, "Le Bloc doit être null après suppression");
    }

    // Test for findByFoyerIdFoyer(long idFoyer)
    @Test
    public void testFindByFoyerIdFoyer() {
        Foyer foyer = Foyer.builder().nomFoyer("Foyer 1").build(); // Créez un objet foyer fictif
        Bloc bloc1 = Bloc.builder().nomBloc("Bloc F1").capaciteBloc(50).foyer(foyer).build();
        Bloc bloc2 = Bloc.builder().nomBloc("Bloc F2").capaciteBloc(75).foyer(foyer).build();

        blocService.addBloc(bloc1);
        blocService.addBloc(bloc2);

        List<Bloc> blocs = blocService.findByFoyerIdFoyer(foyer.getIdFoyer());
        Assertions.assertEquals(2, blocs.size(), "Il doit y avoir 2 blocs liés à ce foyer");

        blocService.removeBloc(bloc1.getIdBloc());
        blocService.removeBloc(bloc2.getIdBloc());
    }

    // Test for findByChambresIdChambre(Long idChambre)
    @Test
    public void testFindByChambresIdChambre() {
        long chambreId = 1L; // Chambre fictive
        Bloc bloc = Bloc.builder().nomBloc("Bloc G").capaciteBloc(300).build();

        Bloc savedBloc = blocService.addBloc(bloc);

        Bloc foundBloc = blocService.findByChambresIdChambre(chambreId);
        Assertions.assertNotNull(foundBloc, "Le Bloc récupéré ne doit pas être null");
        Assertions.assertEquals(savedBloc.getIdBloc(), foundBloc.getIdBloc(), "L'ID du Bloc doit correspondre à celui ajouté");

        blocService.removeBloc(savedBloc.getIdBloc());
    }

}
