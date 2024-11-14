package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.entities.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
class ReservationServiceTestUnit {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    EtudiantRepository etudiantRepository;

    @Autowired
    ChambreRepository chambreRepository;

    @Autowired
    BlocRepository blocRepository;

    private Bloc bloc;
    private Chambre chambre;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        reservationRepository.deleteAll();

        // Initialize test data
        bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc = blocRepository.save(bloc);

        chambre = new Chambre();
        chambre.setNumeroChambre(1L);
        chambre.setTypeChambre(TypeChambre.DOUBLE);
        chambre = chambreRepository.save(chambre);

        etudiant = new Etudiant();
        etudiant.setCinEtudiant(123456789);
        etudiant = etudiantRepository.save(etudiant);
    }

    @Test
    @Order(1)
    @Transactional
    @Rollback
    void testAddReservation() {
        Reservation savedReservation = reservationService.ajouterReservation(bloc.getIdBloc(), etudiant.getCinEtudiant());
        assertNotNull(savedReservation);
        assertNotNull(savedReservation.getIdReservation());
    }

    @Test
    @Order(2)
    void testRetrieveAllReservations() {
        reservationService.ajouterReservation(bloc.getIdBloc(), etudiant.getCinEtudiant());
        List<Reservation> reservations = reservationService.getAllReservations();
        assertNotNull(reservations);
        assertEquals(1, reservations.size());
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback
    void testRetrieveReservationById() {
        Reservation reservation = reservationService.ajouterReservation(bloc.getIdBloc(), etudiant.getCinEtudiant());
        Reservation retrievedReservation = reservationService.getReservationById(reservation.getIdReservation());
        assertNotNull(retrievedReservation);
        assertEquals(reservation.getIdReservation(), retrievedReservation.getIdReservation());
    }

    @Test
    @Order(4)
    @Transactional
    @Rollback
    void testModifyReservation() {
        Reservation reservation = reservationService.ajouterReservation(bloc.getIdBloc(), etudiant.getCinEtudiant());
        reservation.setEstValide(false);
        Reservation updatedReservation = reservationService.updateReservation(reservation);
        assertFalse(updatedReservation.isEstValide());
    }

    @Test
    @Order(5)
    @Transactional
    @Rollback
    void testRemoveReservation() {
        Reservation reservation = reservationService.ajouterReservation(bloc.getIdBloc(), etudiant.getCinEtudiant());
        reservationService.deleteReservation(reservation.getIdReservation());
        assertFalse(reservationRepository.existsById(reservation.getIdReservation()));
    }
}