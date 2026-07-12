package epam.repository;

import epam.domain.Trainer;

import java.util.List;

public interface EntityRepository<T, ID> {

    T save(T entity);
    T findById(ID id);
    T findByUsername(String userName);
    void changePassword(Long id, String newPassword);
    void changePassword(String username, String newPassword);
    T updateProfile(T entity);
    void activate(ID id);
    void deactivate(ID id);
    boolean authenticate(String username, String password);
    void delete(String username);
    default List<Trainer> findAllNotAssignedToTrainee(String traineeUsername) {
        return List.of();
    }
    List<T> findAll();
}
