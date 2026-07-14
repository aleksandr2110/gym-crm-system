package epam.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer extends User {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id")
    private TrainingType specialization;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "trainers_trainees",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    List<Trainee> trainees = new ArrayList<>();

    @OneToMany(mappedBy = "trainer")
    Set<Training> trainings = new HashSet<>();

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
