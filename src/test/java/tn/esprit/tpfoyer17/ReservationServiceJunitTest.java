
package tn.esprit.tpfoyer17;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer17.entities.*;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.*;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UniversiteRepository universiteRepository;
    @Autowired
    private FoyerRepository foyerRepository;

    @Transactional
    @Order(1)
    @Test
    void testAjouterReservation() {
        // Créer et enregistrer un étudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(123456L);
        etudiant.setNomEtudiant("John");
        etudiant.setPrenomEtudiant("Doe");
        etudiant = etudiantRepository.save(etudiant);

        // Créer et enregistrer un bloc
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(20L);
        bloc = blocRepository.save(bloc);

        // Créer et enregistrer une chambre
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambre.setReservations(new HashSet<>());
        chambre.setBloc(bloc);
        chambre.setNumeroChambre(1L);
        chambre = chambreRepository.save(chambre);

        // Vérifier la capacité de la chambre avant d'ajouter
        assertTrue(reservationService.capaciteChambreMaximale(chambre), "La chambre doit avoir de la capacité avant d'ajouter une réservation");

        // Ajouter la réservation
        Reservation reservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        // Vérifier que la réservation a été ajoutée
        assertNotNull(reservation, "La réservation ne doit pas être nulle");

        // Vérifier l'état de la réservation

        // Vérifier que la chambre contient bien la réservation
        Optional<Chambre> updatedChambre = chambreRepository.findById(chambre.getIdChambre());
        assertTrue(updatedChambre.isPresent(), "La chambre doit être présente en base de données");

        // Vérification de la réservation dans la chambre
        Chambre chambreExistante = updatedChambre.get();
        assertTrue(chambreExistante.getReservations().contains(reservation), "La chambre doit contenir la réservation ajoutée");

        // Vérifier que l'étudiant est bien associé à la réservation
        assertTrue(reservation.getEtudiants().contains(etudiant), "L'étudiant doit être associé à la réservation");

        // Optionnel : Vérifier que la réservation est bien dans la base de données
        Optional<Reservation> foundReservation = reservationRepository.findById(reservation.getIdReservation());
        assertTrue(foundReservation.isPresent(), "La réservation doit être présente en base de données");
        assertEquals(reservation.getIdReservation(), foundReservation.get().getIdReservation(), "L'identifiant de la réservation doit correspondre");
    }

    @Test
    @Order(2)

    void testRetrieveAllReservations() {
        // Given
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEtudiant("John");
        etudiant.setPrenomEtudiant("Doe");
        etudiant.setCinEtudiant(143L);
        etudiantRepository.save(etudiant);

        Reservation reservation = new Reservation();

        reservation.setAnneeUniversitaire(LocalDate.now());
        reservation.setEstValide(true);
        reservation.setEtudiants(Set.of(etudiant));
        reservation.setIdReservation("2024-bloc A-20");
        reservationRepository.save(reservation);

        // When
        List<Reservation> reservations = reservationService.retrieveAllReservation();

        // Then
        assertThat(reservations).isNotEmpty(); // Ensure the list is not empty
        if (!reservations.isEmpty()) {
            assertThat(reservations.get(0).getIdReservation()).isEqualTo("2024-bloc A-20");
        }
    }



    @Test
    @Order(3)

    void testUpdateReservation() {
        // Arrange: Create and save a Reservation
        Reservation reservation = new Reservation();
        reservation.setIdReservation("123");
        reservation.setAnneeUniversitaire(LocalDate.of(2024, 9, 1));
        reservation.setEstValide(true);
        reservation = reservationRepository.save(reservation);

        // Act: Update the reservation
        reservation.setEstValide(false); // Change a property for the update
        Reservation updatedReservation = reservationService.updateReservation(reservation);

        // Assert: Verify the updated properties
        assertEquals(reservation.getIdReservation(), updatedReservation.getIdReservation());
        assertEquals(reservation.getAnneeUniversitaire(), updatedReservation.getAnneeUniversitaire());
        assertEquals(reservation.isEstValide(), updatedReservation.isEstValide());
    }

    @Test
    @Order(4)
    void testRetrieveReservation() {
        reservationRepository.deleteAll();

        // Add sample data
        Reservation reservation1 = new Reservation();
        reservation1.setIdReservation("1");
        // Set other properties for reservation1 (e.g., date, guest, etc.)

        Reservation reservation2 = new Reservation();
        reservation2.setIdReservation("2");
        // Set other properties for reservation2

        // Save the test data in the database
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        List<Reservation> result = reservationService.retrieveAllReservation();

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals(2, result.size(), "The number of reservations retrieved should be 2.");
        assertEquals("1", result.get(0).getIdReservation(), "First reservation ID should be '1'.");
        assertEquals("2", result.get(1).getIdReservation(), "Second reservation ID should be '2'.");
    }




    @Order(5)
    @Test
    @Transactional
    void testAnnulerReservation() {
        // Step 1: Set up initial data
        // Create and save a test Etudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(13456L);
        etudiant.setNomEtudiant("John");
        etudiant.setPrenomEtudiant("Doe");

        etudiantRepository.save(etudiant);

        // Create and save a test Reservation
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(LocalDate.now());
        reservation.setEstValide(true);
        reservation.getEtudiants().add(etudiant); // This will work now since the Set is initialized
        reservationRepository.save(reservation);

        // Step 2: Cancel the reservation
        reservationService.annulerReservation(13456L); // Assuming this method exists

        // Step 3: Verify the reservation is cancelled
        Optional<Reservation> canceledReservation = reservationRepository.findById("1");
        Assertions.assertTrue(canceledReservation.isPresent(), "The reservation should exist but be modified.");

        // Test for non-existing student
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            reservationService.annulerReservation(999999); // Using a CIN that does not exist
        });

        Assertions.assertEquals("Etudiant non trouvé avec CIN : 999999", exception.getMessage());
    }




    @Test
    @Order(6)
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(10705L);
        etudiant.setNomEtudiant("naw");
        etudiant.setPrenomEtudiant("ras");
        etudiantRepository.save(etudiant);

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer 1");
        foyerRepository.save(foyer);

        Universite universite = new Universite();
        universite.setNomUniversite("Université 1");
        universite.setFoyer(foyer);
        universiteRepository.save(universite);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setFoyer(foyer);
        bloc = blocRepository.save(bloc);

        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambre.setReservations(new HashSet<>());
        chambre.setBloc(bloc);
        chambre.setNumeroChambre(12288L);
        chambre = chambreRepository.save(chambre);

        Reservation reservation1 = new Reservation();
        reservation1.setIdReservation("1");
        reservation1.getEtudiants().add(etudiant);
        reservation1.setChambre(chambre);
        reservation1.setAnneeUniversitaire(LocalDate.of(2023, 1, 1));
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setIdReservation("1023");
        reservation2.setChambre(chambre);
        reservation2.setAnneeUniversitaire(LocalDate.of(2022, 1, 1));
        reservationRepository.save(reservation2);

        // Act
        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(
                LocalDate.of(2023, 1, 1), "Université 1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation1.getIdReservation(), result.get(0).getIdReservation());
        assertEquals("Université 1", result.get(0).getChambre().getBloc().getFoyer().getUniversite().getNomUniversite());
    }
    @Test
    @Order(8)
    void testGetReservationParAnneeUniversitaireEtNomUniversiteKeyWord() {




        // Act: Call the service method with the specified university name and year
        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversiteKeyWord(
                LocalDate.of(2023, 1, 1), "Université 1");

        // Assert: Verify that only the reservation from 2023 is returned
        assertNotNull(result, "The result should not be null.");
        assertEquals(1, result.size(), "There should be only one reservation returned.");

        // Check if the returned reservation ID matches
        assertEquals("1", result.get(0).getIdReservation(), "The returned reservation ID should match.");

        // Check if the university name matches
        // Assurez-vous que le chemin d'accès à l'université soit correct
        assertEquals("Université 1", result.get(0).getChambre().getBloc().getFoyer().getUniversite().getNomUniversite(),
                "The university name should match the specified university.");
    }


    @Order(9)
    @Test
    void testCapaciteChambreMaximale() {

        // Étape 2 : Créer une chambre (exemple avec un type SIMPLE)
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);

        // Étape 3 : Créer des réservations
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();

        // Étape 4 : Créer un ensemble de réservations
        Set<Reservation> reservations = new HashSet<>();
        reservations.add(reservation1); // Ajouter la première réservation
        reservations.add(reservation2); // Ajouter la seconde réservation

        // Étape 5 : Ajouter les réservations à la chambre
        chambre.setReservations(reservations); // Passer l'ensemble de réservations à la chambre

        // Étape 6 : Appeler la méthode pour vérifier la capacité
        boolean capaciteChambreAvantAjout = reservationService.capaciteChambreMaximale(chambre);

        // Étape 7 : Vérifier que la capacité est atteinte
        assertFalse(capaciteChambreAvantAjout, "La chambre devrait être déjà réservée, donc la capacité doit être false");
    }

}