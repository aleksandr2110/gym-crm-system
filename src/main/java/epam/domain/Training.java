package epam.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Training {

    private Long id;
    private Trainer trainer;
    private List<Trainee> trainers;
    private String trainingName;
    private String trainingType;
    private LocalDateTime trainingDate;
    private String trainingDuration;

    public Training() {
    }

    public Training(Long id, Trainer trainer, List<Trainee> trainers, String trainingName,
                    String trainingType, LocalDateTime trainingDate, String trainingDuration) {
        this.id = id;
        this.trainer = trainer;
        this.trainers = trainers;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public List<Trainee> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainee> trainers) {
        this.trainers = trainers;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDateTime getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDateTime trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(String trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Objects.equals(id, training.id) && Objects.equals(trainer, training.trainer) && Objects.equals(trainers, training.trainers) && Objects.equals(trainingName, training.trainingName) && Objects.equals(trainingType, training.trainingType) && Objects.equals(trainingDate, training.trainingDate) && Objects.equals(trainingDuration, training.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainer, trainers, trainingName, trainingType, trainingDate, trainingDuration);
    }
}
