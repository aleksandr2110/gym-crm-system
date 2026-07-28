package epam.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "trainers")
@AllArgsConstructor
@NoArgsConstructor
public class Trainer extends User {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

//    @NotNull
//    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @MapsId
//    @JoinColumn(name = "user_id", nullable = false)
//    User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "specialization_id", nullable = false)
    private TrainingType specialization;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "trainers_trainees",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    List<Trainee> trainees = new ArrayList<>();

    public void removeAssociations() {
        for (Trainee trainee : new ArrayList<>(trainees)) {
            trainee.getTrainers().remove(this);
        }
        for (Training t : new ArrayList<>(trainings)) {
            t.setTrainer(null);
        }
        trainings.clear();
    }

    @OneToMany(mappedBy = "trainer",cascade = CascadeType.ALL, orphanRemoval = true)
    List<Training> trainings = new ArrayList<>();

    public void addTraining(Training training) {
        trainings.add(training);
    }

    @Generated
    public String toString() {
        String var10000 = String.valueOf(this.getSpecialization());
        return "Trainer(specialization=" + var10000 + ", trainees=" + String.valueOf(this.getTrainees()) + ", trainings=" + String.valueOf(this.getTrainings()) + ")";
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $specialization = this.getSpecialization();
        result = result * 59 + ($specialization == null ? 43 : $specialization.hashCode());
        Object $trainees = this.getTrainees();
        result = result * 59 + ($trainees == null ? 43 : $trainees.hashCode());
        Object $trainings = this.getTrainings();
        result = result * 59 + ($trainings == null ? 43 : $trainings.hashCode());
        return result;
    }
}
