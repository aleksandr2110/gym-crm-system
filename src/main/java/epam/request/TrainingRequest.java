package epam.request;

import epam.domain.InnerDataTraining;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class TrainingRequest {

    private InnerDataTraining innerDataTraining;
    private String trainingType;
    private LocalDateTime trainingDate;
    private String trainingDuration;

    public TrainingRequest() {
    }

    public TrainingRequest(InnerDataTraining innerDataTraining, String trainingType,
                           LocalDateTime trainingDate, String trainingDuration) {
        this.innerDataTraining = innerDataTraining;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public InnerDataTraining getInnerDataTraining() {
        return innerDataTraining;
    }

    public void setInnerDataTraining(InnerDataTraining innerDataTraining) {
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
        TrainingRequest that = (TrainingRequest) o;
        return Objects.equals(innerDataTraining, that.innerDataTraining) && Objects.equals(trainingType, that.trainingType) && Objects.equals(trainingDate, that.trainingDate) && Objects.equals(trainingDuration, that.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerDataTraining, trainingType, trainingDate, trainingDuration);
    }
}
