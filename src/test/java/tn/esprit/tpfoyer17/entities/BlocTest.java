package tn.esprit.tpfoyer17.entities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BlocTest {

    @Test
    void getIdBloc() {
        Bloc bloc = new Bloc();
        assertEquals(0, bloc.getIdBloc());
    }

    @Test
    void getNomBloc() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("A1");
        assertEquals("A1", bloc.getNomBloc());
    }

    @Test
    void getCapaciteBloc() {
        Bloc bloc = new Bloc();
        bloc.setCapaciteBloc(100);
        assertEquals(100, bloc.getCapaciteBloc());
    }

    @Test
    void getFoyer() {
        Bloc bloc = new Bloc();
        Foyer foyer = new Foyer();
        bloc.setFoyer(foyer);
        assertEquals(foyer, bloc.getFoyer());
    }

    @Test
    void getChambres() {
        Bloc bloc = new Bloc();
        Set<Chambre> chambres = new HashSet<>();
        bloc.setChambres(chambres);
        assertEquals(chambres, bloc.getChambres());
    }

    @Test
    void setNomBloc() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("A2");
        assertEquals("A2", bloc.getNomBloc());
    }

    @Test
    void setCapaciteBloc() {
        Bloc bloc = new Bloc();
        bloc.setCapaciteBloc(200);
        assertEquals(200, bloc.getCapaciteBloc());
    }

    @Test
    void setFoyer() {
        Bloc bloc = new Bloc();
        Foyer unFoyer = new Foyer();
        bloc.setFoyer(unFoyer);
        assertEquals(unFoyer, bloc.getFoyer());
    }

    @Test
    void setChambres() {
        Bloc bloc = new Bloc();
        Set<Chambre> chambreHashSet = new HashSet<>();
        bloc.setChambres(chambreHashSet);
        assertEquals(chambreHashSet, bloc.getChambres());
    }

    @Test
    void testToString() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("A1");
        bloc.setCapaciteBloc(100);
        String expected = "Bloc(idBloc=0, nomBloc=A1, capaciteBloc=100)";
        assertEquals(expected, bloc.toString());
    }

    @Test
    void builder() {
        Bloc bloc = Bloc.builder()
                .nomBloc("A1")
                .capaciteBloc(100)
                .build();
        assertEquals("A1", bloc.getNomBloc());
        assertEquals(100, bloc.getCapaciteBloc());
    }
}