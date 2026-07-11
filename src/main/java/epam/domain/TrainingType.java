package epam.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "training_types")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "training_type_name", nullable = false)
    private TrainingTypeName trainingTypeName;

    @OneToMany(mappedBy = "trainingType")
    private Set<Training> trainings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "specialization")
    private List<Trainer> trainers = new ArrayList<>();
}

