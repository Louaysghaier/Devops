package tn.esprit.tpfoyer17;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.services.impementations.EtudiantService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class EtudiantServiceMock {

    @Mock
    private EtudiantRepository mockRepository;

    @InjectMocks
    private EtudiantService etudiantService;


    @Test
    public void testAddEtudiants() {
        // Créer un mock du repository
        EtudiantRepository mockRepository = mock(EtudiantRepository.class);

        // Créer une instance du service avec le mock du repository
        EtudiantService etudiantService = new EtudiantService(mockRepository);

        // Créer un étudiant fictif
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setNomEtudiant("Doe");
        etudiant1.setPrenomEtudiant("John");
        etudiant1.setCinEtudiant(123456);
        etudiant1.setDateNaissance(java.sql.Date.valueOf("2000-01-01"));

        // Simuler la méthode saveAll du repository
        when(mockRepository.saveAll(anyList())).thenReturn(Arrays.asList(etudiant1));

        // Appeler la méthode addEtudiants
        List<Etudiant> savedEtudiants = etudiantService.addEtudiants(Arrays.asList(etudiant1));

        // Vérifier que la méthode saveAll a été appelée
        verify(mockRepository, times(1)).saveAll(anyList());

        // Vérifier les résultats
        assert savedEtudiants != null && savedEtudiants.size() > 0;
        assert savedEtudiants.get(0).getNomEtudiant().equals("Doe");
    }

    @Test

    public void testUpdateEtudiant() {
        // Créer un mock du repository
        EtudiantRepository mockRepository = mock(EtudiantRepository.class);

        // Créer une instance du service avec le mock du repository
        EtudiantService etudiantService = new EtudiantService(mockRepository);

        // Créer un étudiant fictif initial
        Etudiant etudiantInitial = new Etudiant();
        etudiantInitial.setCinEtudiant(123456);  // Assigner un CIN
        etudiantInitial.setNomEtudiant("Doe");
        etudiantInitial.setPrenomEtudiant("John");
        etudiantInitial.setDateNaissance(java.sql.Date.valueOf("2000-01-01"));

        // Créer un étudiant mis à jour
        Etudiant etudiantUpdated = new Etudiant();
        etudiantUpdated.setCinEtudiant(123456);  // Assigner un CIN
        etudiantUpdated.setNomEtudiant("Smith");
        etudiantUpdated.setPrenomEtudiant("Jane");
        etudiantUpdated.setDateNaissance(java.sql.Date.valueOf("2000-01-01"));

        // Simuler la méthode save du repository pour retourner l'étudiant mis à jour
        when(mockRepository.save(any(Etudiant.class))).thenReturn(etudiantUpdated);

        // Appeler la méthode updateEtudiant
        Etudiant updatedEtudiant = etudiantService.updateEtudiant(etudiantInitial);

        // Vérifier que la méthode save a bien été appelée une fois
        verify(mockRepository, times(1)).save(any(Etudiant.class));

        // Vérifier que l'étudiant retourné est le même que celui que nous avons configuré
        assertNotNull(updatedEtudiant);
        assertEquals("Smith", updatedEtudiant.getNomEtudiant(), "Le nom de l'étudiant mis à jour doit être 'Smith'");
        assertEquals("Jane", updatedEtudiant.getPrenomEtudiant(), "Le prénom de l'étudiant mis à jour doit être 'Jane'");
        assertEquals(123456, updatedEtudiant.getCinEtudiant(), "Le CIN de l'étudiant mis à jour doit être 123456");
    }
    @Test
    public void testRetrieveEtudiant() {
        // Créer un mock du repository
        EtudiantRepository mockRepository = mock(EtudiantRepository.class);

        // Créer une instance du service avec le mock du repository
        EtudiantService etudiantService = new EtudiantService(mockRepository);

        // Créer un étudiant fictif à retourner
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(123456);
        etudiant.setNomEtudiant("Doe");
        etudiant.setPrenomEtudiant("John");
        etudiant.setDateNaissance(java.sql.Date.valueOf("2000-01-01"));

        // Simuler la méthode findById du repository pour retourner un étudiant
        when(mockRepository.findById(123456L)).thenReturn(Optional.of(etudiant));

        // Appeler la méthode retrieveEtudiant
        Etudiant retrievedEtudiant = etudiantService.retrieveEtudiant(123456L);

        // Vérifier que la méthode findById a bien été appelée une fois
        verify(mockRepository, times(1)).findById(123456L);

        // Vérifier que l'étudiant retourné est le même que celui que nous avons configuré
        assertNotNull(retrievedEtudiant);
        assertEquals("Doe", retrievedEtudiant.getNomEtudiant(), "Le nom de l'étudiant doit être 'Doe'");
        assertEquals("John", retrievedEtudiant.getPrenomEtudiant(), "Le prénom de l'étudiant doit être 'John'");
        assertEquals(123456, retrievedEtudiant.getCinEtudiant(), "Le CIN de l'étudiant doit être 123456");
    }
    @Test
    public void testRemoveEtudiant() {
        // Créer un mock du repository
        EtudiantRepository mockRepository = mock(EtudiantRepository.class);

        // Créer une instance du service avec le mock du repository
        EtudiantService etudiantService = new EtudiantService(mockRepository);

        // ID de l'étudiant à supprimer
        long idEtudiant = 123456L;

        // Appeler la méthode removeEtudiant
        etudiantService.removeEtudiant(idEtudiant);

        // Vérifier que la méthode deleteById a bien été appelée une fois avec l'ID de l'étudiant
        verify(mockRepository, times(1)).deleteById(idEtudiant);
    }
    @Test
    public void testFindByReservationsAnneeUniversitaire() {
        // Créer un mock du repository
        EtudiantRepository mockRepository = mock(EtudiantRepository.class);

        // Créer une instance du service avec le mock du repository
        EtudiantService etudiantService = new EtudiantService(mockRepository);

        // Créer des étudiants fictifs pour simuler le retour de la méthode findByReservationsAnneeUniversitaire
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setCinEtudiant(123456L);
        etudiant1.setNomEtudiant("John");
        etudiant1.setPrenomEtudiant("Doe");

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setCinEtudiant(654321L);
        etudiant2.setNomEtudiant("Jane");
        etudiant2.setPrenomEtudiant("Smith");

        List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);

        // Simuler le comportement de la méthode findByReservationsAnneeUniversitaire
        when(mockRepository.findByReservationsAnneeUniversitaire(LocalDate.now())).thenReturn(etudiants);

        // Appeler la méthode findByReservationsAnneeUniversitaire
        List<Etudiant> result = etudiantService.findByReservationsAnneeUniversitaire();

        // Vérifier que la méthode findByReservationsAnneeUniversitaire a bien été appelée avec la bonne année
        verify(mockRepository, times(1)).findByReservationsAnneeUniversitaire(LocalDate.now());

        // Vérifier que la liste des étudiants retournée n'est pas vide et contient les bons étudiants
        assertNotNull(result);
        assertEquals(2, result.size(), "Le nombre d'étudiants retournés doit être 2");
        assertEquals("John", result.get(0).getNomEtudiant(), "Le nom du premier étudiant doit être 'John'");
        assertEquals("Jane", result.get(1).getNomEtudiant(), "Le nom du second étudiant doit être 'Jane'");
    }

}