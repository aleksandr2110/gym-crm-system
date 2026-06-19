package epam.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Training {

    private Long TraineeId;
    private Long TrainerId;
    private String trainingName;
    private String trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

    public Training() {
    }

    public Long getTraineeId() {
        return TraineeId;
    }

    public void setTraineeId(Long traineeId) {
        TraineeId = traineeId;
    }

    public Long getTrainerId() {
        return TrainerId;
    }

    public void setTrainerId(Long trainerId) {
        TrainerId = trainerId;
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

    public Duration getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Duration trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Objects.equals(TraineeId, training.TraineeId) && Objects.equals(TrainerId, training.TrainerId) && Objects.equals(trainingName, training.trainingName) && Objects.equals(trainingType, training.trainingType) && Objects.equals(trainingDate, training.trainingDate) && Objects.equals(trainingDuration, training.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TraineeId, TrainerId, trainingName, trainingType, trainingDate, trainingDuration);
    }
}
