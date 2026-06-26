package epam.service;

import epam.domain.Trainee;
import epam.request.TraineeRequest;

public interface TraineeService {

    Trainee create(TraineeRequest traineeRequest);
    Trainee update(TraineeRequest traineeRequest, Long userId);
    Trainee select(Long id);
    void delete(Long id);
}
