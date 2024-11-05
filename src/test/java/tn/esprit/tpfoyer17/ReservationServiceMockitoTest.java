package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ReservationServiceMockitoTest {

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test for addReservation()
    @Test
    void testAjouterReservation() {
        Chambre chambre = new Chambre(); // Assume Chambre is properly initialized
        Etudiant etudiant = new Etudiant(); // Assume Etudiant is properly initialized
        Reservation reservation = Reservation.builder()
                .chambre(chambre)
                .etudiants((Set<Etudiant>) etudiant) // Assuming etudiants is a list
                .build();

        // Define behavior for save() method in mocked repository
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation savedReservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        assertNotNull(savedReservation, "The saved reservation should not be null");
        assertEquals(chambre, savedReservation.getChambre(), "The chambre should match");
        verify(reservationRepository, times(1)).save(reservation); // Verify that save was called once
    }

    // Test for retrieveReservations()
    @Test
    void testRetrieveReservations() {
        Reservation reservation1 = Reservation.builder().build(); // Assume properly initialized
        Reservation reservation2 = Reservation.builder().build(); // Assume properly initialized

        // Define behavior for findAll() in the mocked repository
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        List<Reservation> reservations = reservationService.retrieveAllReservation();
        assertNotNull(reservations);
        assertEquals(2, reservations.size(), "There should be 2 reservations retrieved");

        verify(reservationRepository, times(1)).findAll(); // Verify findAll was called once
    }

    // Test for updateReservation()
    @Test
    void testUpdateReservation() {
        Reservation reservation = Reservation.builder().idReservation(String.valueOf(1L)).build(); // Assume properly initialized

        // Define behavior for save() in mocked repository
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        reservation.setChambre(new Chambre()); // Update chambre or other fields
        Reservation updatedReservation = reservationService.updateReservation(reservation);

        assertEquals(reservation.getIdReservation(), updatedReservation.getIdReservation(), "The reservation ID should match");
        verify(reservationRepository, times(1)).save(reservation); // Verify save was called once
    }

    // Test for retrieveReservation(long id)
    @Test
    void testRetrieveReservation() {
        Reservation reservation = Reservation.builder().idReservation(String.valueOf(1L)).build(); // Assume properly initialized

        // Define behavior for findById() in mocked repository
        when(reservationRepository.findById(String.valueOf(1L))).thenReturn(Optional.of(reservation));

        Reservation retrievedReservation = reservationService.retrieveReservation(String.valueOf(1L));

        assertNotNull(retrievedReservation, "The retrieved reservation should not be null");
        assertEquals(1L, retrievedReservation.getIdReservation(), "The reservation ID should match");
        verify(reservationRepository, times(1)).findById(String.valueOf(1L)); // Verify findById was called once
    }

    // Test for removeReservation(long id)
    @Test
    void testRemoveReservation() {
        // Mock the Reservation to be returned by findById()
        Reservation reservation = Reservation.builder().idReservation(String.valueOf(1L)).build(); // Assume properly initialized
        when(reservationRepository.findById(String.valueOf(1L))).thenReturn(Optional.of(reservation)); // Mock findById() to return a Reservation

        // Mock the behavior of deleteById (no actual delete happening in the test)
        doNothing().when(reservationRepository).deleteById(String.valueOf(1L));

        // Call the service method
        reservationService.retrieveReservation(String.valueOf(1L));

        // Verify that findById() was called once
        verify(reservationRepository, times(1)).findById("1"); // Pass String here

        // Verify that deleteById() was called once
        verify(reservationRepository, times(1)).deleteById(String.valueOf(1L));
    }
}