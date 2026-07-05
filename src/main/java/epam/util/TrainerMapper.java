package epam.util;

import epam.domain.Trainer;
import epam.domain.TrainingType;
import epam.domain.TrainingTypeName;
import epam.request.TrainerDTO;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    public Trainer toModel(TrainerDTO trainerDto) {
        var trainer = new Trainer();
        trainer.setFirstName(trainerDto.getFirstName());
        trainer.setLastName(trainerDto.getLastName());
        var trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeName.valueOf(trainerDto.getSpecialization()));
        trainer.setSpecialization(trainingType);
        trainer.setIsActive(trainerDto.getActive());
        return trainer;
    }

}
