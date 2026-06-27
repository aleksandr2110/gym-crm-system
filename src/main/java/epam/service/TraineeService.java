package epam.service;

import epam.domain.Trainee;
import epam.request.TraineeDTO;

public interface TraineeService {

    Trainee create(TraineeDTO traineeDTO);
    Trainee update(TraineeDTO traineeDTO, Long userId);
    Trainee select(Long id);
    void delete(Long id);
}
