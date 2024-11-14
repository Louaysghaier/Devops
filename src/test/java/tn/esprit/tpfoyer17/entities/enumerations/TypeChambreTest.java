package tn.esprit.tpfoyer17.entities.enumerations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeChambreTest {

    @Test
    void values() {
        TypeChambre[] expectedValues = {TypeChambre.SIMPLE, TypeChambre.DOUBLE, TypeChambre.SIMPLE, TypeChambre.TRIPLE};
        assertArrayEquals(expectedValues, TypeChambre.values());
    }

    @Test
    void valueOf() {
        assertEquals(TypeChambre.SIMPLE, TypeChambre.valueOf("SIMPLE"));
        assertEquals(TypeChambre.DOUBLE, TypeChambre.valueOf("DOUBLE"));
        assertEquals(TypeChambre.SIMPLE, TypeChambre.valueOf("SINGLE"));
        assertEquals(TypeChambre.TRIPLE, TypeChambre.valueOf("TRIPLE"));
    }
}