package tn.esprit.tpfoyer17;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceMockTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private UniversiteRepository universiteRepository;
    @Mock
    private Chambre chambre;

    @Mock
    private Bloc bloc;
    private Etudiant etudiant;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock data
        etudiant = new Etudiant();
        etudiant.setCinEtudiant(123456L);
        etudiant.setReservations(new HashSet<>());

        reservation = new Reservation();
        reservation.setIdReservation("101-BLOC-2024");
        reservation.setEtudiants(new HashSet<>());
        reservation.getEtudiants().add(etudiant); // Add the student to the reservation

        chambre = new Chambre();
        chambre.setNumeroChambre(101);
        bloc=new Bloc();
        bloc.setNomBloc("universite 1");
        chambre.setBloc(bloc);
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambre.setReservations(new HashSet<>());
        chambre.getReservations().add(reservation);

        // Link the reservation to the student
        etudiant.getReservations().add(reservation);
    }

    @Test
    void testRetrieveAllReservation() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        List<Reservation> reservations = reservationService.retrieveAllReservation();
        assertEquals(1, ((List<?>) reservations).size());
        assertEquals(reservation, reservations.get(0));
    }

    @Test
    void testUpdateReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        Reservation updatedReservation = reservationService.updateReservation(reservation);
        assertEquals(reservation, updatedReservation);
    }

    @Test
    void testRetrieveReservation() {
        when(reservationRepository.findById("101-BLOC-2024")).thenReturn(Optional.of(reservation));
        Reservation foundReservation = reservationService.retrieveReservation("101-BLOC-2024");
        assertEquals(reservation, foundReservation);
    }

    @Test
    void testAnnulerReservation_EtudiantNotFound() {
        assertThrows(IllegalArgumentException.class, () -> reservationService.annulerReservation(123456));
    }
    @Transactional
    @Test

    void testAnnulerReservation() {
        // Setup: Define test variables
        long studentCin = 123456L;

        // Mock the Etudiant retrieval
        when(etudiantRepository.findByCinEtudiant(studentCin)).thenReturn(Optional.of(etudiant));
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);

        // Call the method under test
        reservationService.annulerReservation(studentCin);

        // Verify interactions
        verify(etudiantRepository, times(1)).findByCinEtudiant(studentCin);
        verify(reservationRepository, times(1)).save(reservation);
        verify(chambreRepository, times(1)).findByReservationsIdReservation(reservation.getIdReservation());

        // Verify the student is removed from the reservation
        assertFalse(reservation.getEtudiants().contains(etudiant));

        // Verify the reservation is validated based on the room type
        assertTrue(reservation.isEstValide()); // Assuming SIMPLE type means the reservation is valid if one student is removed
    }


    @Test
    void testAnnulerReservation_EtudiantNonTrouve() {
        long studentCin = 999999L; // A CIN that does not exist

        // Mock the Etudiant retrieval to return empty
        when(etudiantRepository.findByCinEtudiant(studentCin)).thenReturn(Optional.empty());

        // Assert that an exception is thrown
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.annulerReservation(studentCin);
        });
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        when(reservationRepository.recupererParBlocEtTypeChambre("Universite1", 2024)).thenReturn(List.of(reservation));
        List<Reservation> reservations = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(LocalDate.of(2024, 1, 1), "Universite1");
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
    }




    @Test
    void testAjouterReservation_EtudiantNotFound() {
        // Arrange
        when(etudiantRepository.findByCinEtudiant(123456)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.ajouterReservation(1L, 123456);
        });

        assertEquals("Etudiant non trouvé avec CIN : 123456", exception.getMessage());
    }

    @Test
    void testGenerateId() {
        String id = reservationService.generateId(101, "BLOC");
        assertEquals("101-BLOC-2024", id);
    }

    @Test

    void testCapaciteChambreMaximale() {
        // Arrange


        // Test case 1: Simple room with two reservations
        Chambre simpleChambre = mock(Chambre.class);
        when(simpleChambre.getTypeChambre()).thenReturn(TypeChambre.SIMPLE);
        when(simpleChambre.getReservations()).thenReturn(new HashSet<>() {{
            add(new Reservation());
            add(new Reservation());
        }});

        boolean result1 = reservationService.capaciteChambreMaximale(simpleChambre);
        assertFalse(result1, "A simple room with two reservations should not accept more reservations.");

        // Test case 2: Double room with two reservations
        Chambre doubleChambre = mock(Chambre.class);
        when(doubleChambre.getTypeChambre()).thenReturn(TypeChambre.DOUBLE);
        when(doubleChambre.getReservations()).thenReturn(new HashSet<>() {{
            add(new Reservation());
            add(new Reservation());
        }});

        boolean result2 = reservationService.capaciteChambreMaximale(doubleChambre);
        assertTrue(result2, "A double room with two reservations can accept more.");

        // Test case 3: Triple room with three reservations
        Chambre tripleChambre = mock(Chambre.class);
        when(tripleChambre.getTypeChambre()).thenReturn(TypeChambre.TRIPLE);
        when(tripleChambre.getReservations()).thenReturn(new HashSet<>() {{
            add(new Reservation());
            add(new Reservation());
            add(new Reservation());
        }});

        boolean result3 = reservationService.capaciteChambreMaximale(tripleChambre);
        assertTrue(result3, "A triple room with three reservations should not accept more reservations.");
    }
    @Test
    void testAjouterReservation_Success() {
        // Mock the Etudiant and Chambre repositories to return the objects
        when(etudiantRepository.findByCinEtudiant(123456L)).thenReturn(Optional.of(etudiant));
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the ajouterReservation method
        Reservation reservation1 = reservationService.ajouterReservation(1L, 123456L);

        // Verify the reservation is not null and contains the correct student
        assertNotNull(reservation1, "The reservation should not be null");
        assertTrue(reservation1.getEtudiants().contains(etudiant), "The reservation should contain the correct student");


    }







}