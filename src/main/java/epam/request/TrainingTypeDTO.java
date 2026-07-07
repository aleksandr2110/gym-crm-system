package epam.request;

import epam.domain.TrainingTypeName;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeDTO {

    private Long id;
    private TrainingTypeName trainingTypeName;

    public TrainingTypeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingTypeName getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(TrainingTypeName trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}
