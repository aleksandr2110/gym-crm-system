package epam.util;

import epam.domain.TrainingType;
import epam.request.TrainingTypeDTO;

public class TrainingTypeMapper {

    public TrainingType toModel(TrainingTypeDTO trainingTypeDto) { {
        var trainingType = new TrainingType();
        trainingType.setId(trainingTypeDto.getId());
        trainingType.setTrainingTypeName(trainingTypeDto.getTrainingTypeName());
        return trainingType;
    }}
}
