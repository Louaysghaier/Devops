package tn.esprit.tpfoyer17.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer17.entities.Reservation;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String> {
    Reservation findByIdReservation(String id);


    @Query("SELECT DISTINCT reservation FROM Reservation reservation ," +
            " Universite universite" +
            " INNER JOIN universite.foyer.blocs bloc" +
            " INNER JOIN bloc.chambres chambre" +
            " INNER JOIN chambre.reservations" +
            " WHERE universite.nomUniversite = :nomUniversite" +
            " AND YEAR(reservation.anneeUniversitaire) = YEAR( :anneeUniversitaire)")
    List<Reservation> recupererParBlocEtTypeChambre(
            @Param("nomUniversite") String nomUniversite,
            @Param("anneeUniversitaire") LocalDate anneeUniversitaire);

    @Query("select r from Reservation r join Chambre c on r member of c.reservations where (c.bloc.idBloc = :idBloc and year(r.anneeUniversitaire) = year(current_date) and r.estValide = true) order by r.idReservation limit 1")
    Reservation findForReservation(long idBloc);

    @Query("select r from Reservation r join Etudiant e on e member of r.etudiants where e.cinEtudiant = :cinEtudiant and year(r.anneeUniversitaire) = :anneeUniversitaire")
    Reservation findByEtudiantsCinEtudiantAndAnneeUniversitaire(long cinEtudiant, int anneeUniversitaire);
    @Query("select r from Reservation r join Chambre c on r member of c.reservations where extract(year from r.anneeUniversitaire) = :anneeUniversitaire and c.bloc.foyer.universite.nomUniversite = :nomUniversite ")
    List<Reservation> findByAnneeUniversitaire_YearAndNomUnuiversite(int anneeUniversitaire, String nomUniversite);

}
