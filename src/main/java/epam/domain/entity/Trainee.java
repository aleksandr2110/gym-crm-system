package epam.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "trainees")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@PrimaryKeyJoinColumn(name = "user_id")
public class Trainee extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @ManyToMany(mappedBy = "trainees")
    private List<Trainer> trainers = new ArrayList<>();


    public void removeAssociations() {
        for (Trainer trainer : new ArrayList<>(trainers)) {
            trainer.getTrainees().remove(this);
        }
        for (Training t : new ArrayList<>(trainings)) {
            t.setTrainee(null);
        }
        trainings.clear();
    }

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    public void addTraining(Training training) {
        trainings.add(training);
    }

}
