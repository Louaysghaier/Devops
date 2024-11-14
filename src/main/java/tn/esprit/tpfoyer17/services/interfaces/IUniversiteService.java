package tn.esprit.tpfoyer17.services.interfaces;

import tn.esprit.tpfoyer17.entities.Universite;

import java.util.List;

public interface IUniversiteService {
    List<Universite> retrieveAllUniversities();
    Universite addUniversity (Universite u);
    Universite updateUniversity (Universite u);
    Universite retrieveUniversity (long idUniversity);
    Universite desaffecterFoyerAUniversite (  long idUniversite) ;
    Universite affecterFoyerAUniversite (long idFoyer, String nomUniversite) ;

}
