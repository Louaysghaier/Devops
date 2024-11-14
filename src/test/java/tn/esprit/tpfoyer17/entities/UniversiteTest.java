package tn.esprit.tpfoyer17.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniversiteTest {

    @Test
    void getIdUniversite() {
        Universite uneUniversite = new Universite();
        assertEquals(0, uneUniversite.getIdUniversite());
    }

    @Test
    void getNomUniversite() {
        Universite universite = new Universite();
        universite.setNomUniversite("University01");
        assertEquals("University01", universite.getNomUniversite());
    }

    @Test
    void getAdresse() {
        Universite universite = new Universite();
        universite.setAdresse("1234 Main St");
        assertEquals("1234 Main St", universite.getAdresse());
    }

    @Test
    void getFoyer() {
        Universite universite = new Universite();
        Foyer unFoyer = new Foyer();
        universite.setFoyer(unFoyer);
        assertEquals(unFoyer, universite.getFoyer());
    }

    @Test
    void setNomUniversite() {
        Universite universite = new Universite();
        universite.setNomUniversite("University1");
        assertEquals("University1", universite.getNomUniversite());
    }

    @Test
    void setAdresse() {
        Universite universite = new Universite();
        universite.setAdresse("123 Main St");
        assertEquals("123 Main St", universite.getAdresse());
    }

    @Test
    void setFoyer() {
        Universite universite = new Universite();
        Foyer foyer = new Foyer();
        universite.setFoyer(foyer);
        assertEquals(foyer, universite.getFoyer());
    }

    @Test
    void testToString() {
        Universite universite = new Universite();
        universite.setNomUniversite("University1");
        universite.setAdresse("123 Main St");
        String expected = "Universite(idUniversite=0, nomUniversite=University1, adresse=123 Main St)";
        assertEquals(expected, universite.toString());
    }

    @Test
    void builder() {
        Universite universite = Universite.builder()
                .nomUniversite("University1")
                .adresse("123 Main St")
                .build();
        assertEquals("University1", universite.getNomUniversite());
        assertEquals("123 Main St", universite.getAdresse());
    }
}