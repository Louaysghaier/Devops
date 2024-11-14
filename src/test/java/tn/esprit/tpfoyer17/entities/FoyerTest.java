package tn.esprit.tpfoyer17.entities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FoyerTest {

    @Test
    void getIdFoyer() {
        Foyer foyer = new Foyer();
        assertEquals(0, foyer.getIdFoyer());
    }

    @Test
    void getNomFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer01");
        assertEquals("Foyer01", foyer.getNomFoyer());
    }

    @Test
    void getCapaciteFoyer() {
        Foyer foyer = new Foyer();
        foyer.setCapaciteFoyer(201);
        assertEquals(201, foyer.getCapaciteFoyer());
    }

    @Test
    void getUniversite() {
        Foyer foyer = new Foyer();
        Universite uneUniversite = new Universite();
        foyer.setUniversite(uneUniversite);
        assertEquals(uneUniversite, foyer.getUniversite());
    }

    @Test
    void getBlocs() {
        Foyer foyer = new Foyer();
        Set<Bloc> blocHashSet = new HashSet<>();
        foyer.setBlocs(blocHashSet);
        assertEquals(blocHashSet, foyer.getBlocs());
    }

    @Test
    void setNomFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer1");
        assertEquals("Foyer1", foyer.getNomFoyer());
    }

    @Test
    void setCapaciteFoyer() {
        Foyer foyer = new Foyer();
        foyer.setCapaciteFoyer(200);
        assertEquals(200, foyer.getCapaciteFoyer());
    }

    @Test
    void setUniversite() {
        Foyer foyer = new Foyer();
        Universite universite = new Universite();
        foyer.setUniversite(universite);
        assertEquals(universite, foyer.getUniversite());
    }

    @Test
    void setBlocs() {
        Foyer foyer = new Foyer();
        Set<Bloc> blocs = new HashSet<>();
        foyer.setBlocs(blocs);
        assertEquals(blocs, foyer.getBlocs());
    }

    @Test
    void testToString() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer1");
        foyer.setCapaciteFoyer(200);
        String expected = "Foyer(idFoyer=0, nomFoyer=Foyer1, capaciteFoyer=200)";
        assertEquals(expected, foyer.toString());
    }

    @Test
    void builder() {
        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer1")
                .capaciteFoyer(200)
                .build();
        assertEquals("Foyer1", foyer.getNomFoyer());
        assertEquals(200, foyer.getCapaciteFoyer());
    }
}