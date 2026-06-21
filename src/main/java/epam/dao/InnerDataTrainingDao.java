package epam.dao;

import java.util.Objects;

public class InnerDataTrainingDao {

    private String trainerId;
    private String traineeId;
    private String trainingName;

    public InnerDataTrainingDao() {
    }

    public InnerDataTrainingDao(String trainerId, String trainingName, String traineeId) {
        this.trainerId = trainerId;
        this.trainingName = trainingName;
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

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InnerDataTrainingDao that = (InnerDataTrainingDao) o;
        return Objects.equals(trainerId, that.trainerId) && Objects.equals(traineeId, that.traineeId) && Objects.equals(trainingName, that.trainingName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerId, traineeId, trainingName);
    }
}
