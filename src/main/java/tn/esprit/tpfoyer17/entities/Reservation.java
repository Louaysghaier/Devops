package tn.esprit.tpfoyer17.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation implements Serializable {

    @Id
    String idReservation;

    LocalDate anneeUniversitaire;

    boolean estValide;

    @ToString.Exclude
    @ManyToOne // Assuming a reservation is linked to one chambre
    @JoinColumn(name = "chambre_id") // Update this to match your database schema
    private Chambre chambre;

    // Optional: To explicitly show the methods
    @Setter
    @Getter
    @ToString.Exclude
    @ManyToMany
    @JsonIgnore
    private Set<Etudiant> etudiants = new HashSet<>(); // Initialize to avoid null

}