package tn.esprit.tpfoyer17.services.impementations;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.*;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.*;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        log.info("AutoCloseable: {}", autoCloseable);
    }

    @Test
    void getAllReservations() {
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(reservations, result);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getReservationById() {
        String id = "1";
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById(id);

        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).findById(id);
    }

    @Test
    void deleteReservation() {
        String id = "1";

        reservationService.deleteReservation(id);

        verify(reservationRepository, times(1)).deleteById(id);
    }

    @Test
    void updateReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.updateReservation(reservation);

        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void ajouterReservation() {
        long idBloc = 1L;
        long cinEtudiant = 12345678L;
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(cinEtudiant);
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        Bloc bloc = new Bloc();
        bloc.setNomBloc("A");
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setEtudiants(new HashSet<>(Collections.singletonList(etudiant)));

        when(reservationRepository.findForReservation(idBloc)).thenReturn(null);
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.getForReservation(idBloc)).thenReturn(chambre);
        when(blocRepository.findById(idBloc)).thenReturn(Optional.of(bloc));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.ajouterReservation(idBloc, cinEtudiant);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void annulerReservation() {
        long cinEtudiant = 12345678L;
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(cinEtudiant);
        Reservation reservation = new Reservation();
        reservation.setEtudiants(new HashSet<>(Collections.singletonList(etudiant)));

        when(reservationRepository.findByEtudiantsCinEtudiantAndAnneeUniversitaire(cinEtudiant, Year.now().getValue())).thenReturn(reservation);
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.annulerReservation(cinEtudiant);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void getReservationParAnneeUniversitaireEtNomUniversite() {
        Date anneeUniversitaire = new Date();
        String nomUniversite = "Test University";
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(anneeUniversitaire);

        when(reservationRepository.findByAnneeUniversitaire_YearAndNomUnuiversite(calendar.get(Calendar.YEAR), nomUniversite)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversitaire, nomUniversite);

        assertEquals(reservations, result);
        verify(reservationRepository, times(1)).findByAnneeUniversitaire_YearAndNomUnuiversite(calendar.get(Calendar.YEAR), nomUniversite);
    }
}