package tn.esprit.tpfoyer17;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import java.util.Optional;
import java.util.Set;

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
    @Mock
    private UniversiteRepository universiteRepository;

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
        // Create a Bloc instance
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");

        // Create a Chambre instance and set its Bloc
        Chambre chambre = new Chambre();
        chambre.setBloc(bloc);
        chambre.setTypeChambre(TypeChambre.DOUBLE);

        // Create a new Etudiant instance
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(12345678L);

        // Mock repository behaviors
        when(etudiantRepository.findByCinEtudiant(etudiant.getCinEtudiant())).thenReturn(etudiant);
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        // Create a reservation and save it
        Reservation reservation = Reservation.builder()
                .idReservation("reservationId")
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .chambre(chambre)
                .etudiants(new HashSet<Etudiant>() {{ add(etudiant); }}) // Add the student
                .build();

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Perform the action
        Reservation result = reservationService.ajouterReservation(etudiant.getCinEtudiant(), chambre.getIdChambre());

        // Assertions
        assertNotNull(result, "The reservation result should not be null.");
        assertEquals(reservation.getIdReservation(), result.getIdReservation(), "The reservation ID should match.");
        verify(etudiantRepository, times(1)).findByCinEtudiant(etudiant.getCinEtudiant());
        verify(chambreRepository, times(1)).save(chambre);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testAnnulerReservation() {
        long cinEtudiant = 12345678L;
        Etudiant etudiant = new Etudiant();
        Set<Reservation> reservations = new HashSet<>();

        // Create a reservation with a proper ID and initialize the etudiants set
        Reservation reservation = Reservation.builder()
                .idReservation("reservationId")
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .etudiants(new HashSet<>()) // Ensure etudiants is initialized
                .build();

        Chambre chambre = new Chambre();
        chambre.setReservations(reservations);

        reservations.add(reservation);
        etudiant.setReservations(reservations);

        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);

        // Perform the action
        Reservation result = reservationService.annulerReservation(cinEtudiant);

        // Adjust the assertion based on your expected behavior
        assertNotNull(result); // Or assertNull depending on your logic
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        LocalDate anneeUniversite = LocalDate.of(2023, 1, 1);
        String nomUniversite = "Esprit";

        reservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversite, nomUniversite);

        verify(reservationRepository, times(1)).recupererParBlocEtTypeChambre(nomUniversite, anneeUniversite);
    }
}