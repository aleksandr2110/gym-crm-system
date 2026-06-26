package epam.dao;

import epam.domain.Trainee;
import epam.domain.Trainer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TrainingDao {

    private Long id;
    private Trainer trainer;
    private List<Trainee> trainers;
    private String trainingName;
    private String trainingType;
    private LocalDateTime trainingDate;
    private String trainingDuration;

    public TrainingDao() {
    }

    public TrainingDao(Long id, List<Trainee> trainers, Trainer trainer, String trainingType,
                       String trainingName, LocalDateTime trainingDate, String trainingDuration) {
        this.id = id;
        this.trainers = trainers;
        this.trainer = trainer;
        this.trainingType = trainingType;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Trainee> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainee> trainers) {
        this.trainers = trainers;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrainingDao that = (TrainingDao) o;
        return Objects.equals(id, that.id) && Objects.equals(trainer, that.trainer) && Objects.equals(trainers, that.trainers) && Objects.equals(trainingName, that.trainingName) && Objects.equals(trainingType, that.trainingType) && Objects.equals(trainingDate, that.trainingDate) && Objects.equals(trainingDuration, that.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainer, trainers, trainingName, trainingType, trainingDate, trainingDuration);
    }
}
