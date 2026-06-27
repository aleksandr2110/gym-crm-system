package epam.service;

import epam.domain.Trainee;

public interface TraineeService {

    Trainee create(Trainee trainee);
    Trainee update(Trainee trainee, Long userId);
    Trainee select(Long id);
    void delete(Long id);
}
