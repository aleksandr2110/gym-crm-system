package epam.util;

import epam.domain.Training;
import epam.request.TrainingDTO;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public Training toModel(TrainingDTO trainingDto) {
        var training = new Training();
        training.setTrainingName(trainingDto.getTrainingName());
        training.setTrainingType(trainingDto.getTrainingType());
        training.setTrainingDate(trainingDto.getTrainingDate());
        training.setTrainingDuration(trainingDto.getTrainingDuration());

        return training;
    }

}
