package epam.dao;

import java.time.LocalDateTime;
import java.util.Objects;

public class TrainingDao {

    private InnerDataTrainingDao innerDataTraining;
    private String trainingType;
    private LocalDateTime trainingDate;
    private String trainingDuration;

    public TrainingDao() {
    }

    public TrainingDao(String trainingType, LocalDateTime trainingDate, String trainingDuration) {
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public InnerDataTrainingDao getInnerDataTraining() {
        return innerDataTraining;
    }

    public void setInnerDataTraining(InnerDataTrainingDao innerDataTraining) {
        this.innerDataTraining = innerDataTraining;
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
        TrainingDao that = (TrainingDao) o;
        return Objects.equals(trainingType, that.trainingType) && Objects.equals(trainingDate, that.trainingDate) && Objects.equals(trainingDuration, that.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingType, trainingDate, trainingDuration);
    }
}
