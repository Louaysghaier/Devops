package tn.esprit.tpfoyer17;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;

public class ReservationServiceMockitoTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    public void testAjouterReservation_Success() {
        // Arrange: Set up the test data and mock behavior
        Etudiant etudiant = new Etudiant();  // Initialize with required fields if necessary
        Reservation reservation = new Reservation();  // Initialize with required fields if necessary

        // Mock the save behavior to return the same reservation object
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act: Call the method under test
        Reservation result = reservationService.ajouterReservation(etudiant.getIdEtudiant(), Long.parseLong(reservation.getIdReservation()));

        // Assert: Verify the outcome
        assertNotNull(result, "The reservation should not be null after saving");
        verify(reservationRepository, times(1)).save(reservation);  // Ensure save was called once

        // Add more assertions as needed to check specific values or state
    }

    @Test
    public void testAjouterReservation_Failure() {
        // Arrange: Prepare a scenario for failure, e.g., repository throws an exception
        Etudiant etudiant = new Etudiant();
        Reservation reservation = new Reservation();

        // Simulate an exception when saving
        when(reservationRepository.save(any(Reservation.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert: Expect an exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            reservationService.ajouterReservation(etudiant.getIdEtudiant(), Long.parseLong(reservation.getIdReservation()));
        });

        assertEquals("Database error", exception.getMessage());
        verify(reservationRepository, times(1)).save(reservation);  // Ensure save was attempted once
    }
}
