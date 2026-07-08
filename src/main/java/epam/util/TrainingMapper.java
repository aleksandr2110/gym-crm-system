package epam.util;

import epam.domain.Training;
import epam.domain.TrainingType;
import epam.domain.TrainingTypeName;
import epam.request.TrainingDTO;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public Training toModel(TrainingDTO trainingDto) {
        var training = new Training();
        training.setTrainingName(trainingDto.getTrainingName());

        var trainingType = new TrainingType();
        trainingType.setTrainingTypeName(
                TrainingTypeName.getByName(trainingDto.getTrainingType().toUpperCase()));
        training.setTrainingType(trainingType);

        training.setTrainingDate(trainingDto.getTrainingDate());
        training.setTrainingDuration(trainingDto.getTrainingDuration());

        return training;
    }

}
