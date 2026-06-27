package epam.util;

import epam.domain.Trainer;
import epam.request.TrainerDTO;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    public Trainer toModel(TrainerDTO trainerDto) {
        var trainer = new Trainer();
        trainer.setFirstName(trainerDto.getFirstName());
        trainer.setLastName(trainerDto.getLastName());
        trainer.setSpecialization(trainerDto.getSpecialization());
        trainer.setActive(trainerDto.getActive());
        return trainer;
    }

}
