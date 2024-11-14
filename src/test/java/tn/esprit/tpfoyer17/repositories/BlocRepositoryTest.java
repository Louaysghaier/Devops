package tn.esprit.tpfoyer17.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.entities.Universite;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BlocRepositoryTest {

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private UniversiteRepository universiteRepository;

    @Test
    void findByFoyerUniversiteIdUniversite() {
        // Create a Universite
        Universite universite = new Universite();
        universite.setNomUniversite("University1");
        universite.setAdresse("123 Main St");

        // Create a Foyer
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer1");
        foyer.setCapaciteFoyer(200);
        foyer.setUniversite(universite);

        // Save the Universite and Foyer
        universiteRepository.save(universite);
        foyer = foyerRepository.save(foyer);

        // Create a Bloc
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc1");
        bloc.setFoyer(foyer);

        // Save the Bloc
        blocRepository.save(bloc);

        Optional<Bloc> optionalBloc = blocRepository.findByNomBloc("Bloc1");
        assertTrue(optionalBloc.isPresent());

    }
}