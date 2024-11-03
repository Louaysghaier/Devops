package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.services.impementations.BlocService;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class BlocServiceMockTest {

    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    BlocService blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test for addBloc()
    @Test
    void testAjouterBloc() {
        Bloc bloc = Bloc.builder().nomBloc("Bloc A").capaciteBloc(100).build();

        // Define behavior for save() method in mocked repository
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc savedBloc = blocService.addBloc(bloc);

        assertNotNull(savedBloc, "The saved bloc should not be null");
        assertEquals("Bloc A", savedBloc.getNomBloc(), "The bloc name should match");
        verify(blocRepository, times(1)).save(bloc); // Verify that save was called once
    }

    // Test for retrieveBlocs()
    @Test
    void testRetrieveBlocs() {
        Bloc bloc1 = Bloc.builder().nomBloc("Bloc B1").capaciteBloc(50).build();
        Bloc bloc2 = Bloc.builder().nomBloc("Bloc B2").capaciteBloc(75).build();

        // Define behavior for findAll() in the mocked repository
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        List<Bloc> blocs = blocService.retrieveBlocs();
        assertNotNull(blocs);
        assertEquals(2, blocs.size(), "There should be 2 blocs retrieved");

        verify(blocRepository, times(1)).findAll(); // Verify findAll was called once
    }

    // Test for updateBloc()
    @Test
    void testUpdateBloc() {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc C").capaciteBloc(120).build();

        // Define behavior for save() in mocked repository
        when(blocRepository.save(bloc)).thenReturn(bloc);

        bloc.setCapaciteBloc(200); // Update capacity
        Bloc updatedBloc = blocService.updateBloc(bloc);

        assertEquals(200, updatedBloc.getCapaciteBloc(), "The bloc capacity should be updated to 200");
        verify(blocRepository, times(1)).save(bloc); // Verify save was called once
    }

    // Test for retrieveBloc(long idBloc)
    @Test
    void testRetrieveBloc() {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc D").capaciteBloc(150).build();

        // Define behavior for findById() in mocked repository
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc retrievedBloc = blocService.retrieveBloc(1L);

        assertNotNull(retrievedBloc, "The retrieved bloc should not be null");
        assertEquals(1L, retrievedBloc.getIdBloc(), "The bloc ID should match");
        verify(blocRepository, times(1)).findById(1L); // Verify findById was called once
    }

    // Test for removeBloc(long idBloc)
    @Test
    void testRemoveBloc() {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc E").capaciteBloc(200).build();

        // No need to mock deleteById as it returns void, just verify if it was called
        doNothing().when(blocRepository).deleteById(1L);

        blocService.removeBloc(1L);

        verify(blocRepository, times(1)).deleteById(1L); // Verify deleteById was called once
    }

    // Test for findByFoyerIdFoyer(long idFoyer)
    @Test
    void testFindByFoyerIdFoyer() {
        Bloc bloc1 = Bloc.builder().nomBloc("Bloc F1").capaciteBloc(50).build();
        Bloc bloc2 = Bloc.builder().nomBloc("Bloc F2").capaciteBloc(75).build();

        // Define behavior for findByFoyerIdFoyer in the mocked repository
        when(blocRepository.findByFoyerIdFoyer(1L)).thenReturn(Arrays.asList(bloc1, bloc2));

        List<Bloc> blocs = blocService.findByFoyerIdFoyer(1L);
        assertEquals(2, blocs.size(), "There should be 2 blocs related to the foyer");

        verify(blocRepository, times(1)).findByFoyerIdFoyer(1L); // Verify method call
    }

    // Test for findByChambresIdChambre(Long idChambre)
    @Test
    void testFindByChambresIdChambre() {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc G").capaciteBloc(300).build();

        // Define behavior for findByChambresIdChambre in the mocked repository
        when(blocRepository.findByChambresIdChambre(1L)).thenReturn(bloc);

        Bloc foundBloc = blocService.findByChambresIdChambre(1L);
        assertNotNull(foundBloc, "The retrieved bloc should not be null");
        assertEquals(1L, foundBloc.getIdBloc(), "The bloc ID should match");

        verify(blocRepository, times(1)).findByChambresIdChambre(1L); // Verify method call
    }

}
