package epam.service;

import epam.domain.Trainee;

public interface TraineeService {

    Trainee save(Trainee trainee);
    Trainee findById(Long id);
    Trainee findByUsername(String userName); //
    void changePassword(Long id, String newPassword); //
    void changePassword(String username, String newPassword);
    Trainee updateProfile(Trainee entity, Long userId);
    void activate(Long id);
    void deactivate(Long id);
    Trainee authenticateTrainee(String username, String password);
    void deleteProfile(String username);
}
