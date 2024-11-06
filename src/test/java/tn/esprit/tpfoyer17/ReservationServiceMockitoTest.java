package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class ReservationServiceMockitoTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    EtudiantRepository etudiantRepository;

    @Autowired
    ChambreRepository chambreRepository;
    @Autowired
    ReservationRepository reservationRepository;

    // Test for adding a reservation
    @Test
    @Order(1)
    public void testAjouterReservation() {
        // Create an Etudiant and Chambre
        Etudiant etudiant = new Etudiant("Dupont", "Jean", 12345678);
        Chambre chambre = new Chambre(1, TypeChambre.SIMPLE, 0, 2);

        // Save entities to simulate the addition
        etudiantRepository.save(etudiant);
        chambreRepository.save(chambre);

        // Add reservation
        Reservation reservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        // Validate reservation
        Assertions.assertNotNull(reservation);
        Assertions.assertEquals(chambre.getIdChambre(), reservation.getChambre().getIdChambre());
        Assertions.assertTrue(reservation.getEtudiants().contains(etudiant));

        // Cleanup
        reservationService.reservationRepository.delete(reservation);
    }

    // Test for retrieving all reservations
    @Test
    @Order(2)
    public void testRetrieveReservations() {
        // Create reservations for testing
        Etudiant etudiant1 = new Etudiant("Dupont", "Jean", 12345678);
        Etudiant etudiant2 = new Etudiant("Martin", "Pierre", 87654321);

        Chambre chambre1 = new Chambre(1, TypeChambre.SIMPLE, 1, 2);
        Chambre chambre2 = new Chambre(2, TypeChambre.DOUBLE, 1, 2);

        etudiantRepository.save(etudiant1);
        etudiantRepository.save(etudiant2);
        chambreRepository.save(chambre1);
        chambreRepository.save(chambre2);

        // Create reservations
        reservationService.ajouterReservation(chambre1.getIdChambre(), etudiant1.getCinEtudiant());
        reservationService.ajouterReservation(chambre2.getIdChambre(), etudiant2.getCinEtudiant());

        // Retrieve all reservations
        List<Reservation> reservations = reservationService.retrieveAllReservation();

        Assertions.assertNotNull(reservations);
        Assertions.assertFalse(reservations.isEmpty());

        // Cleanup
        reservations.forEach(reservation -> reservationService.reservationRepository.delete(reservation));
    }

    // Test for deleting a reservation
    @Test
    @Order(3)
    public void testDeleteReservation() {
        // Create reservation for testing
        Etudiant etudiant = new Etudiant("Dupont", "Jean", 12345678);
        Chambre chambre = new Chambre(1, TypeChambre.SIMPLE, 1, 2);

        etudiantRepository.save(etudiant);
        chambreRepository.save(chambre);

        Reservation reservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        // Delete the reservation
        reservationService.reservationRepository.delete(reservation);

        // Ensure the reservation is deleted
        Reservation deletedReservation = reservationService.retrieveReservation(reservation.getIdReservation());
        Assertions.assertNull(deletedReservation);
    }
}
