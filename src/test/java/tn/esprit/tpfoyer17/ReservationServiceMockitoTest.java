package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceMockitoTest {

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
    private Etudiant etudiant;

    @Mock
    private Chambre chambre;

    @Mock
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        // Initialisation de Mockito pour injecter les mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllReservations() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        // Act
        var reservations = reservationService.retrieveAllReservation();

        // Assert
        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testRetrieveReservation() {
        // Arrange
        String idReservation = "123-ABC-2024";
        when(reservationRepository.findById(idReservation)).thenReturn(Optional.of(reservation));

        // Act
        var result = reservationService.retrieveReservation(idReservation);

        // Assert
        assertNotNull(result);
        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).findById(idReservation);
    }

    @Test
    public void testAnnulerReservation() {
        // Arrange
        long cinEtudiant = 123456;
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(etudiant.getReservations()).thenReturn(Set.of(reservation));

        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        when(chambreRepository.findByReservationsIdReservation(anyString())).thenReturn(chambre);

        // Act
        Reservation result = reservationService.annulerReservation(cinEtudiant);

        // Assert
        assertNotNull(result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testAjouterReservation() {
        long cinEtudiant = 12345678L;
        String idReservation = "RES123"; // Example ID
        long idChambre = 1L;

        // Create a mock Etudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(cinEtudiant);
        etudiant.setNomEtudiant("John");
        etudiant.setPrenomEtudiant("Doe");

        // Create a mock Bloc
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");

        // Create a mock Chambre
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101);
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambre.setMaxCapacity(2);
        chambre.setBloc(bloc);
        chambre.setReservations(new HashSet<>());

        // Create a mock Reservation
        Reservation reservation = new Reservation();
        reservation.setIdReservation(idReservation); // Set the ID
        reservation.setChambre(chambre);
        reservation.getEtudiants().add(etudiant); // Correctly add the Etudiant to the Set

        // Mock repository behaviors
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        // Call the method under test
        Reservation createdReservation = reservationService.ajouterReservation(idChambre, cinEtudiant);

        // Assertions
        assertNotNull(createdReservation, "Reservation should not be null");
        assertEquals(idReservation, createdReservation.getIdReservation(), "Reservation ID should match");
    }

   /* @Test
    public void testCapaciteChambreMaximale() {
        // Arrange
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambre.setReservations(new HashSet<>());

        // Act
        boolean isCapaciteMaximale = reservationService.capaciteChambreMaximale(chambre);

        // Assert
        assertTrue(isCapaciteMaximale);
    }*/
}
