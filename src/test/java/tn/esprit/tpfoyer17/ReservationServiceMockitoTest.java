package tn.esprit.tpfoyer17;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer17.entities.*;
import tn.esprit.tpfoyer17.repositories.*;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ReservationServiceMockitoTest {

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

    private Reservation reservation;
    private Etudiant etudiant;
    private Chambre chambre;
    private Bloc bloc;


    @Mock
    private Set<Reservation> reservationList; // Mock this list

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(chambre.getBloc()).thenReturn(bloc);
        when(bloc.getNomBloc()).thenReturn("Bloc A");

        // Initialize mock data
        etudiant = new Etudiant();
        chambre = new Chambre();
        reservation = new Reservation("1-Bloc1-2024", LocalDate.now(), true, chambre, new HashSet<>());

        // Inject mocks into the service
        reservationService = new ReservationService(reservationRepository, etudiantRepository, chambreRepository, universiteRepository);
    }

    @Test
    public void testRetrieveReservation() {
        when(reservationRepository.findById("1-Bloc1-2024")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.retrieveReservation("1-Bloc1-2024");

        assertNotNull(result);
        assertEquals("1-Bloc1-2024", result.getIdReservation());
    }

    @Test
    public void testAjouterReservation() {
        long cinEtudiant = 12345L;
        long idChambre = 1L;

        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.ajouterReservation(idChambre, cinEtudiant);

        assertNotNull(result);
        assertTrue(result.isEstValide());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testAnnulerReservation() {
        long cinEtudiant = 12345L;

        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(reservationRepository.findById("1-Bloc1-2024")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.annulerReservation(cinEtudiant);

        assertNull(result); // Assumes the result is null when the cancellation is successful
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testUpdateReservation() {
        Reservation updatedReservation = new Reservation("1-Bloc1-2024", LocalDate.now(), false, chambre, new HashSet<>());

        when(reservationRepository.save(updatedReservation)).thenReturn(updatedReservation);

        Reservation result = reservationService.updateReservation(updatedReservation);

        assertNotNull(result);
        assertFalse(result.isEstValide());
        verify(reservationRepository, times(1)).save(updatedReservation);
    }
}
