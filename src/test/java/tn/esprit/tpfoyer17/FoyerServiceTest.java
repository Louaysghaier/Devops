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
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.services.impementations.FoyerService;
import tn.esprit.tpfoyer17.services.interfaces.IBlocService;
import tn.esprit.tpfoyer17.services.interfaces.IChambreService;
import tn.esprit.tpfoyer17.services.interfaces.IFoyerService;
import tn.esprit.tpfoyer17.services.interfaces.IUniversiteService;

import java.util.List;
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer. OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles("test")
public class FoyerServiceTest {
    @Autowired
    IFoyerService foyerService;
    @Autowired
    IBlocService blocService;
    @Autowired
    IUniversiteService universiteService;
}
