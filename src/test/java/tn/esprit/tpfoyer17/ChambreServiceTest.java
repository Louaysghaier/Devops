package tn.esprit.tpfoyer17.services.implementations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChambreServiceTest {

    @InjectMocks
    private ChambreService chambreService;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @Test
    public void testRetrieveAllChambres() {
        // Arrange
        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        when(chambreRepository.findAll()).thenReturn(Arrays.asList(chambre1, chambre2));

        // Act
        List<Chambre> chambres = chambreService.retrieveAllChambres();

        // Assert
        assertNotNull(chambres);
        assertEquals(2, chambres.size());
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
    public void testAffecterChambresABloc() {
        // Arrange
        long idBloc = 1L;
        List<Long> numChambre = Arrays.asList(101L, 102L);
        Bloc bloc = new Bloc();
        List<Chambre> chambreList = Arrays.asList(new Chambre(), new Chambre());

        when(blocRepository.findById(idBloc)).thenReturn(Optional.of(bloc));
        when(chambreRepository.findByNumeroChambreIn(numChambre)).thenReturn(chambreList);

        // Act
        Bloc result = chambreService.affecterChambresABloc(numChambre, idBloc);

        // Assert
        assertNotNull(result);
        assertEquals(bloc, result);
        verify(blocRepository, times(1)).findById(idBloc);
        verify(chambreRepository, times(1)).findByNumeroChambreIn(numChambre);
        verify(chambreRepository, times(2)).save(any(Chambre.class));
    }

    @Test
    public void testGetChambresParNomUniversite() {
        // Arrange
        String nomUniversite = "ESPRIT";
        List<Chambre> chambreList = Arrays.asList(new Chambre(), new Chambre());
        when(chambreRepository.findByBlocFoyerUniversiteNomUniversiteLike(nomUniversite)).thenReturn(chambreList);

        // Act
        List<Chambre> result = chambreService.getChambresParNomUniversite(nomUniversite);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findByBlocFoyerUniversiteNomUniversiteLike(nomUniversite);
    }

    @Test
    public void testGetChambresNonReserve() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.getChambresNonReserve()).thenReturn(Arrays.asList(chambre));

        // Act
        chambreService.getChambresNonReserve();

        // Assert
        verify(chambreRepository, times(1)).getChambresNonReserve();
    }
}
