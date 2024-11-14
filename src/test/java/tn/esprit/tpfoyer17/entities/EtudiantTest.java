package tn.esprit.tpfoyer17.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EtudiantTest {

    @Test
    void getIdEtudiant() {
        Etudiant etudiant = new Etudiant();
        assertEquals(0, etudiant.getIdEtudiant());
    }

    @Test
    void getNomEtudiant() {
        Etudiant unEtudiant = new Etudiant();
        unEtudiant.setNomEtudiant("John");
        assertEquals("John", unEtudiant.getNomEtudiant());
    }

    @Test
    void getPrenomEtudiant() {
        Etudiant unEtudiant = new Etudiant();
        unEtudiant.setPrenomEtudiant("Doe");
        assertEquals("Doe", unEtudiant.getPrenomEtudiant());
    }

    @Test
    void getCinEtudiant() {
        Etudiant unEtudiant = new Etudiant();
        unEtudiant.setCinEtudiant(12345678L);
        assertEquals(12345678L, unEtudiant.getCinEtudiant());
    }

    @Test
    void getDateNaissance() {
        Etudiant unEtudiant = new Etudiant();
        Date date = new Date();
        unEtudiant.setDateNaissance(date);
        assertEquals(date, unEtudiant.getDateNaissance());
    }

    @Test
    void getReservations() {
        Etudiant unEtudiant = new Etudiant();
        Set<Reservation> reservations = new HashSet<>();
        unEtudiant.setReservations(reservations);
        assertEquals(reservations, unEtudiant.getReservations());
    }

    @Test
    void setNomEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEtudiant("John");
        assertEquals("John", etudiant.getNomEtudiant());
    }

    @Test
    void setPrenomEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setPrenomEtudiant("Doe");
        assertEquals("Doe", etudiant.getPrenomEtudiant());
    }

    @Test
    void setCinEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(12345678L);
        assertEquals(12345678L, etudiant.getCinEtudiant());
    }

    @Test
    void setDateNaissance() {
        Etudiant etudiant = new Etudiant();
        Date date = new Date();
        etudiant.setDateNaissance(date);
        assertEquals(date, etudiant.getDateNaissance());
    }

    @Test
    void setReservations() {
        Etudiant etudiant = new Etudiant();
        Set<Reservation> reservations = new HashSet<>();
        etudiant.setReservations(reservations);
        assertEquals(reservations, etudiant.getReservations());
    }

    @Test
    void testToString() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEtudiant("John");
        etudiant.setPrenomEtudiant("Doe");
        etudiant.setCinEtudiant(12345678L);
        etudiant.setDateNaissance(new Date());
        String expected = "Etudiant(idEtudiant=0, nomEtudiant=John, prenomEtudiant=Doe, cinEtudiant=12345678, dateNaissance=" + etudiant.getDateNaissance()+")";
        assertEquals(expected, etudiant.toString());
    }

    @Test
    void builder() {
        Etudiant etudiant = Etudiant.builder()
                .nomEtudiant("John")
                .prenomEtudiant("Doe")
                .cinEtudiant(12345678L)
                .dateNaissance(new Date())
                .build();
        assertEquals("John", etudiant.getNomEtudiant());
        assertEquals("Doe", etudiant.getPrenomEtudiant());
        assertEquals(12345678L, etudiant.getCinEtudiant());
        assertNotNull(etudiant.getDateNaissance());
    }
}