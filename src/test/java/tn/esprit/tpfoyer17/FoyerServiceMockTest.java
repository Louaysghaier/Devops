package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;
import tn.esprit.tpfoyer17.services.impementations.FoyerService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoyerServiceMockTest {

    @InjectMocks
    private FoyerService foyerService;

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllFoyers() {
        // Arrange
        List<Foyer> foyers = new ArrayList<>();
        foyers.add(new Foyer());
        when(foyerRepository.findAll()).thenReturn(foyers);

        // Act
        List<Foyer> result = foyerService.retrieveAllFoyers();

        // Assert
        assertEquals(foyers.size(), result.size());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    void testAddFoyer() {
        // Arrange
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        // Act
        Foyer result = foyerService.addFoyer(foyer);

        // Assert
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testUpdateFoyer() {
        // Arrange
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        // Act
        Foyer result = foyerService.updateFoyer(foyer);

        // Assert
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testRetrieveFoyer() {
        // Arrange
        long idFoyer = 1L;
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(idFoyer)).thenReturn(Optional.of(foyer));

        // Act
        Foyer result = foyerService.retrieveFoyer(idFoyer);

        // Assert
        assertNotNull(result);
        verify(foyerRepository, times(1)).findById(idFoyer);
    }

    @Test
    void testRetrieveFoyer_NotFound() {
        // Arrange
        long idFoyer = 1L;
        when(foyerRepository.findById(idFoyer)).thenReturn(Optional.empty());

        // Act
        Foyer result = foyerService.retrieveFoyer(idFoyer);

        // Assert
        assertNull(result);
        verify(foyerRepository, times(1)).findById(idFoyer);
    }

    @Test
    void testRemoveFoyer() {
        // Arrange
        long idFoyer = 1L;

        // Act
        foyerService.removeFoyer(idFoyer);

        // Assert
        verify(foyerRepository, times(1)).deleteById(idFoyer);
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        // Arrange
        Foyer foyer = new Foyer();
        long idUniversite = 1L;
        Universite universite = new Universite();
        when(universiteRepository.findById(idUniversite)).thenReturn(Optional.of(universite));
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        // Add blocs to the foyer for testing
        Bloc bloc1 = new Bloc();
        bloc1.setFoyer(foyer);
        Set<Bloc> blocs = new HashSet<>();
        blocs.add(bloc1);
        foyer.setBlocs(blocs);

        // Act
        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, idUniversite);

        // Assert
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
        verify(universiteRepository, times(1)).save(universite);
        verify(blocRepository, times(1)).save(bloc1);
    }
}
