package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.services.impementations.ChambreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ChambreServiceTest {

    @Mock
    ChambreRepository chambreRepository;

    @InjectMocks
    ChambreService chambreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test for addChambre()
    @Test
    void testAddChambre() {
        Chambre chambre = Chambre.builder().numeroChambre(101L).typeChambre(TypeChambre.SINGLE).build();

        // Define behavior for save() method in mocked repository
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre savedChambre = chambreService.addChambre(chambre);

        assertNotNull(savedChambre, "The saved chambre should not be null");
        assertEquals(101L, savedChambre.getNumeroChambre(), "The chambre number should match");
        verify(chambreRepository, times(1)).save(chambre); // Verify that save was called once
    }

    // Test for retrieveAllChambres()
    @Test
    void testRetrieveAllChambres() {
        Chambre chambre1 = Chambre.builder().numeroChambre(102L).typeChambre(TypeChambre.DOUBLE).build();
        Chambre chambre2 = Chambre.builder().numeroChambre(103L).typeChambre(TypeChambre.SUITE).build();

        // Define behavior for findAll() in the mocked repository
        when(chambreRepository.findAll()).thenReturn(Arrays.asList(chambre1, chambre2));

        List<Chambre> chambres = chambreService.retrieveAllChambres();
        assertNotNull(chambres);
        assertEquals(2, chambres.size(), "There should be 2 chambres retrieved");

        verify(chambreRepository, times(1)).findAll(); // Verify findAll was called once
    }

    // Test for updateChambre()
    @Test
    void testUpdateChambre() {
        Chambre chambre = Chambre.builder().idChambre(1L).numeroChambre(104L).typeChambre(TypeChambre.SINGLE).build();

        // Define behavior for save() in mocked repository
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        chambre.setNumeroChambre(105L); // Update chambre number
        Chambre updatedChambre = chambreService.updateChambre(chambre);

        assertEquals(105L, updatedChambre.getNumeroChambre(), "The chambre number should be updated to 105");
        verify(chambreRepository, times(1)).save(chambre); // Verify save was called once
    }

    // Test for retrieveChambre(long idChambre)
    @Test
    void testRetrieveChambre() {
        Chambre chambre = Chambre.builder().idChambre(1L).numeroChambre(106L).typeChambre(TypeChambre.SUITE).build();

        // Define behavior for findById() in mocked repository
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre retrievedChambre = chambreService.retrieveChambre(1L);

        assertNotNull(retrievedChambre, "The retrieved chambre should not be null");
        assertEquals(1L, retrievedChambre.getIdChambre(), "The chambre ID should match");
        verify(chambreRepository, times(1)).findById(1L); // Verify findById was called once
    }

    // Additional tests can be added as needed, such as for custom query methods or special cases

}
