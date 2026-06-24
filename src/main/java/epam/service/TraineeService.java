package epam.service;

import epam.domain.Trainee;
import epam.request.TraineeRequest;

public interface TraineeService {

    Trainee create(TraineeRequest traineeRequest);
    Trainee update(TraineeRequest traineeRequest, String username);
    Trainee select(String id);
    void delete(String id);
}
