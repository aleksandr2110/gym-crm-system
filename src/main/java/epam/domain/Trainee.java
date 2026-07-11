package epam.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainees")
public class Trainee extends User {

    @Column(name = "date_of_birth", nullable = true)
    LocalDate dateOfBirth;

    @Column(name = "address", nullable = true)
    String address;

    @ManyToMany(mappedBy = "trainees")
    List<Trainer> trainers = new ArrayList<>();

    @OneToMany(mappedBy = "trainee")
    Set<Training> trainings = new LinkedHashSet<>();

}
