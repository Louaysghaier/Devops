package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ReservationServiceMockitoTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ChambreRepository   chambreRepository;
    @Mock
    EtudiantRepository etudiantRepository;
    @InjectMocks
    ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test for addReservation()
    @Test
    void testAjouterReservation() {
        long idChambre = 1L; // Example ID for Chambre
        long cinEtudiant = 123456789L; // Example CIN for Etudiant

        Chambre chambre = new Chambre(); // Initialize as per your needs
        chambre.setNumeroChambre(101); // Set necessary properties
        Bloc bloc = new Bloc(); // Initialize Bloc
        bloc.setNomBloc("A"); // Set properties
        chambre.setBloc(bloc); // Associate Bloc with Chambre

        Etudiant etudiant = new Etudiant(); // Initialize Etudiant
        etudiant.setCinEtudiant(cinEtudiant); // Set CIN

        // Mocking repository behavior
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        // Create a new reservation with mocked data
        String numReservation = "101A"; // Example generated ID
        Reservation reservation = Reservation.builder()
                .idReservation(numReservation)
                .etudiants(new HashSet<>())
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .build();

        // Mock findById to return an empty Optional (to mimic a new reservation)
        when(reservationRepository.findById(numReservation)).thenReturn(Optional.empty());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Call the method under test
        Reservation savedReservation = reservationService.ajouterReservation(idChambre, cinEtudiant);

        // Assertions to verify the expected behavior
        assertNotNull(savedReservation, "The saved reservation should not be null");
        assertEquals(numReservation, savedReservation.getIdReservation(), "The reservation ID should match");

        // Verify that the necessary repository methods were called
        verify(etudiantRepository, times(1)).findByCinEtudiant(cinEtudiant);
        verify(chambreRepository, times(1)).findById(idChambre);
        verify(reservationRepository, times(1)).save(savedReservation);
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
        Chambre chambre = new Chambre(); // Assume Chambre is properly initialized
        Etudiant etudiant = new Etudiant(); // Assume Etudiant is properly initialized

        // Create a Set of Etudiants
        Set<Etudiant> etudiants = new HashSet<>(Arrays.asList(etudiant)); // Use a Set

        Reservation reservation = Reservation.builder()
                .idReservation("1") // Assuming ID is a String
                .chambre(chambre)
                .etudiants(etudiants) // Use Set here
                .build();

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
        Reservation reservation = Reservation.builder().idReservation("1").build();
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));
        doNothing().when(reservationRepository).deleteById("1");

        // Correct method call to remove the reservation
        reservationService.retrieveReservation("1");

        verify(reservationRepository, times(1)).findById("1");
        verify(reservationRepository, times(1)).deleteById("1");
    }
}