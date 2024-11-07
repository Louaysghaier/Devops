package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private ChambreService chambreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllChambres() {
        // Arrange
        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        List<Chambre> chambreList = Arrays.asList(chambre1, chambre2);
        when(chambreRepository.findAll()).thenReturn(chambreList);

        // Act
        List<Chambre> result = chambreService.retrieveAllChambres();

        // Assert
        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    public void testAddChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        // Act
        Chambre result = chambreService.addChambre(chambre);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    public void testUpdateChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        // Act
        Chambre result = chambreService.updateChambre(chambre);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    public void testRetrieveChambre() {
        // Arrange
        long idChambre = 1L;
        Chambre chambre = new Chambre();
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        // Act
        Chambre result = chambreService.retrieveChambre(idChambre);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).findById(idChambre);
    }

    @Test
    public void testFindByTypeChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.DOUBLE);
        when(chambreRepository.findByTypeChambreAndReservationsEstValide(TypeChambre.DOUBLE, true))
                .thenReturn(Collections.singletonList(chambre));

        // Act
        List<Chambre> result = chambreService.findByTypeChambre();

        // Assert
        assertEquals(1, result.size());
        assertEquals(TypeChambre.DOUBLE, result.get(0).getTypeChambre());
        verify(chambreRepository, times(1))
                .findByTypeChambreAndReservationsEstValide(TypeChambre.DOUBLE, true);
    }







 
}
