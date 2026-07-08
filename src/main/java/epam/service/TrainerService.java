package epam.service;

import epam.domain.Trainer;

import java.util.List;

public interface TrainerService {

    Trainer save(Trainer trainer);
    Trainer findById(Long id);
    Trainer findByUsername(String userName);
    void changePassword(Long id, String newPassword);
    void changePassword(String username, String newPassword);
    Trainer updateProfile(Trainer entity, Long userId);
    void activate(Long id);
    void deactivate(Long id);
    boolean authenticateTrainer(String username, String password);
    void delete(String username);
    List<Trainer> findAllNotAssignedToTrainee(String traineeUsername);
}
