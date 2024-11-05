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
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.entities.Reservation;
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

import static org.mockito.Mockito.when;
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
        long idChambre = 1L;
        long cinEtudiant = 12345678L;
        Etudiant etudiant = new Etudiant();
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambre.setNumeroChambre(1L);
        chambre.setReservations(new HashSet<>());

        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));
        when(reservationRepository.findById(anyString())).thenReturn(Optional.empty());

        Reservation result = reservationService.ajouterReservation(idChambre, cinEtudiant);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(etudiantRepository, times(1)).findByCinEtudiant(cinEtudiant);
        verify(chambreRepository, times(1)).findById(idChambre);
    }

    @Test
    void testAnnulerReservation() {
        long cinEtudiant = 12345678L;
        Etudiant etudiant = new Etudiant();
        Set<Reservation> reservations = new HashSet<>();

        // Use the builder to create a reservation with an ID
        Reservation reservation = Reservation.builder()
                .idReservation("reservationId")
                .anneeUniversitaire(LocalDate.now()) // Set other properties as needed
                .estValide(true)
                .build();

        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.DOUBLE);
        chambre.setReservations(reservations);

        reservations.add(reservation);
        etudiant.setReservations(reservations);

        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);

        Reservation result = reservationService.annulerReservation(cinEtudiant);

        // Adjust the assertion based on the expected behavior
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
