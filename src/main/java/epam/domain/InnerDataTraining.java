package epam.domain;

import java.util.Objects;

public class InnerDataTraining {

    String traineeId;
    String trainerId;
    String trainingName;

    public InnerDataTraining() {
    }

    public InnerDataTraining(String traineeId, String trainerId, String trainingName) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
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
        InnerDataTraining that = (InnerDataTraining) o;
        return Objects.equals(traineeId, that.traineeId) && Objects.equals(trainerId, that.trainerId) && Objects.equals(trainingName, that.trainingName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, trainerId, trainingName);
    }

    @Override
    public String toString() {
        return "InnerDataTraining{" +
                "traineeId='" + traineeId + '\'' +
                ", trainerId='" + trainerId + '\'' +
                ", trainingName='" + trainingName + '\'' +
                '}';
    }
}
