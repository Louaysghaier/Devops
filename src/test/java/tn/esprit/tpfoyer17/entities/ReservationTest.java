package tn.esprit.tpfoyer17.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void getIdReservation() {
        Reservation uneReservation = new Reservation();
        assertNull(uneReservation.getIdReservation());
    }

    @Test
    void getAnneeUniversitaire() {
        Reservation uneReservation = new Reservation();
        Date date = new Date();
        uneReservation.setAnneeUniversitaire(date);
        assertEquals(date, uneReservation.getAnneeUniversitaire());
    }

    @Test
    void isEstValide() {
        Reservation uneReservation = new Reservation();
        uneReservation.setEstValide(true);
        assertTrue(uneReservation.isEstValide());
    }

    @Test
    void getEtudiants() {
        Reservation reservation = new Reservation();
        Set<Etudiant> etudiantHashSet = new HashSet<>();
        reservation.setEtudiants(etudiantHashSet);
        assertEquals(etudiantHashSet, reservation.getEtudiants());
    }

    @Test
    void setIdReservation() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("123");
        assertEquals("123", reservation.getIdReservation());
    }

    @Test
    void setAnneeUniversitaire() {
        Reservation reservation = new Reservation();
        Date date = new Date();
        reservation.setAnneeUniversitaire(date);
        assertEquals(date, reservation.getAnneeUniversitaire());
    }

    @Test
    void setEstValide() {
        Reservation reservation = new Reservation();
        reservation.setEstValide(true);
        assertTrue(reservation.isEstValide());
    }

    @Test
    void setEtudiants() {
        Reservation reservation = new Reservation();
        Set<Etudiant> etudiants = new HashSet<>();
        reservation.setEtudiants(etudiants);
        assertEquals(etudiants, reservation.getEtudiants());
    }

    @Test
    void testToString() {
        Reservation reservation = new Reservation();
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
        String expected = "Reservation(idReservation=null, anneeUniversitaire=" + reservation.getAnneeUniversitaire() + ", estValide=true)";
        assertEquals(expected, reservation.toString());
    }

    @Test
    void builder() {
        Date date = new Date();
        Set<Etudiant> etudiants = new HashSet<>();
        Reservation reservation = Reservation.builder()
                .anneeUniversitaire(date)
                .estValide(true)
                .etudiants(etudiants)
                .build();
        assertEquals(date, reservation.getAnneeUniversitaire());
        assertTrue(reservation.isEstValide());
        assertEquals(etudiants, reservation.getEtudiants());
    }
}