package epam.dao;

import java.util.Objects;

public class TrainingTypeDao {

    String trainingTypeName;

    public TrainingTypeDao() {
    }

    public TrainingTypeDao(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrainingTypeDao that = (TrainingTypeDao) o;
        return Objects.equals(trainingTypeName, that.trainingTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(trainingTypeName);
    }
}
