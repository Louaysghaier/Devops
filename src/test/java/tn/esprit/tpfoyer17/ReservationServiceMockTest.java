package tn.esprit.tpfoyer17;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReservationServiceMockTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private EtudiantRepository etudiantRepository;
    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllReservation() {
        reservationService.retrieveAllReservation();
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveReservation() {
        String idReservation = "123";
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(idReservation)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.retrieveReservation(idReservation);

        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).findById(idReservation);
    }

    @Test
    void testAjouterReservation() {
        long cinEtudiant = 12345678L; // Ensure this matches the stub
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(cinEtudiant);

        // Set up Chambre and Reservation
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.DOUBLE);

        // Mock repository behavior
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findById(anyLong())).thenReturn(Optional.of(chambre)); // Assuming you have a method to find by ID
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the reservation passed

        // Call the method under test
        Reservation result = reservationService.ajouterReservation(cinEtudiant, chambre.getIdChambre());

        // Assertions to verify expected behavior
        assertNotNull(result, "The result should not be null");
        assertEquals(cinEtudiant, result.getEtudiants().iterator().next().getCinEtudiant(), "The student CIN should match");
        verify(etudiantRepository).findByCinEtudiant(cinEtudiant);
        verify(chambreRepository).findById(anyLong());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testAnnulerReservation() {
        long cinEtudiant = 12345678L;
        Etudiant etudiant = new Etudiant();
        Set<Reservation> reservations = new HashSet<>();

        // Create a Chambre instance and set its TypeChambre
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.DOUBLE); // Ensure TypeChambre is initialized
        chambre.setReservations(reservations);

        // Create a reservation and include the etudiant
        Reservation reservation = Reservation.builder()
                .idReservation("reservationId")
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .chambre(chambre) // Set the chambre
                .etudiants(new HashSet<>(Set.of(etudiant))) // Ensure etudiants is initialized
                .build();

        reservations.add(reservation);
        etudiant.setReservations(reservations);

        // Mock repository behavior
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);

        // Perform the action
        reservationService.annulerReservation(cinEtudiant);

        // Assertions
        assertFalse(reservation.isEstValide(), "Reservation should be invalid after cancellation");
        verify(reservationRepository, times(1)).save(reservation); // Verify save was called with the correct reservation
        verify(chambreRepository, times(1)).findByReservationsIdReservation(reservation.getIdReservation());
    }
    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        LocalDate anneeUniversite = LocalDate.of(2023, 1, 1);
        String nomUniversite = "Esprit";

        // Optionally mock a return value if your method returns something
        List<Reservation> expectedReservations = new ArrayList<>();
        when(reservationRepository.recupererParBlocEtTypeChambre(nomUniversite, anneeUniversite))
                .thenReturn(expectedReservations);

        // Perform the action
        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversite, nomUniversite);

        // Assertions to verify expected behavior
        assertEquals(expectedReservations, result, "The returned reservations should match the expected list");
        verify(reservationRepository, times(1)).recupererParBlocEtTypeChambre(nomUniversite, anneeUniversite);
    }
}