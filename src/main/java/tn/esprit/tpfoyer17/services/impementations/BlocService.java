package tn.esprit.tpfoyer17.services.impementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.services.interfaces.IBlocService;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlocService implements IBlocService {
    BlocRepository blocRepository;
    ChambreRepository chambreRepository;

    @Override
    public List<Bloc> retrieveBlocs() {
        return (List<Bloc>) blocRepository.findAll();
    }

    @Override
    public Bloc updateBloc(Bloc bloc) {
        return blocRepository.save(bloc);
    }

    @Override
    public Bloc addBloc(Bloc bloc) {
        return blocRepository.save(bloc);
    }

    @Override
    public Bloc retrieveBloc(long idBloc) {
        return blocRepository.findById(idBloc).orElse(null);
    }

    @Override

    @Transactional
    public void removeBloc(long idBloc) {
        // Fetch the Bloc by its ID
        Bloc bloc = blocRepository.findById(idBloc).orElseThrow(() -> new EntityNotFoundException("Bloc not found"));

        // Get associated Chambres and disassociate them
        Set<Chambre> chambres = bloc.getChambres();
        if (chambres != null && !chambres.isEmpty()) {
            for (Chambre chambre : chambres) {
                chambre.setBloc(null);  // Disassociate the Chambre from the Bloc
                chambreRepository.save(chambre);  // Save the updated Chambre to the database
            }
        }

        // Now it's safe to delete the Bloc
        blocRepository.deleteById(idBloc);
    }


    @Override
    public List<Bloc> findByFoyerIdFoyer(long idFoyer) {
        return blocRepository.findByFoyerIdFoyer(idFoyer);
    }

    @Override
    public Bloc findByChambresIdChambre(Long idChambre) {
        return blocRepository.findByChambresIdChambre(idChambre);
    }
}
