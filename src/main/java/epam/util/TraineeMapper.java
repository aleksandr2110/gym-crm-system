package epam.util;

import epam.domain.Trainee;
import epam.request.TraineeDTO;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {

    public Trainee toModel(TraineeDTO traineeDto) {
        var trainee = new Trainee();
        trainee.setFirstName(traineeDto.getFirstName());
        trainee.setLastName(traineeDto.getLastName());
        trainee.setActive(traineeDto.getActive());
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
        trainee.setAddress(traineeDto.getAddress());
        return trainee;
    }
}
