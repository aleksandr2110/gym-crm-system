package epam.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@PrimaryKeyJoinColumn(name = "user_id") // Links to parent table ID
public class Trainee extends User {

    @Id
    //@Column(name = "id", nullable = false) // user_id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @MapsId
//    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

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

//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Trainee trainee = (Trainee) o;
//        return isActive == trainee.isActive && Objects.equals(id, trainee.id) && Objects.equals(user, trainee.user) && Objects.equals(dateOfBirth, trainee.dateOfBirth) && Objects.equals(address, trainee.address) && Objects.equals(trainers, trainee.trainers) && Objects.equals(trainings, trainee.trainings);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, user, isActive, dateOfBirth, address, trainers, trainings);
//    }
}
