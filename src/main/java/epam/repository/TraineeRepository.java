package epam.repository;

import epam.domain.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("traineeRepository")
public class TraineeRepository implements EntityRepository<Trainee, Long> {

    private final EntityManager entityManager;

    @Autowired
    public TraineeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Trainee save(Trainee trainee) {
        if (trainee.getId() == null) {
            entityManager.persist(trainee);
        } else {
            trainee = entityManager.merge(trainee);
        }
        log.info("Trainee saved with username: {}", trainee.getUserName());
        return trainee;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        var entity = entityManager.find(Trainee.class, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<Trainee> findByUsername(String userName) {
        TypedQuery<Trainee> query = entityManager.createQuery(
                        "FROM Trainee t WHERE t.userName = :userName", Trainee.class)
                .setParameter("userName", userName);
        return query.getResultStream().findAny();
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        var entity = findById(id).orElseThrow(()
                -> new IllegalArgumentException("Trainee not found with id: " + id));

        entity.setPassword(newPassword);
        entityManager.merge(entity);
        log.info("Password changed for trainee with id: {}", id);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Trainee entity = findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("Trainee not found with username: " + username));

        entity.setPassword(newPassword);
        entityManager.merge(entity);
        log.info("Password changed for trainee with user name: {}", username);
    }

    public void activate(Long id) {
        var entity = findById(id).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found with id: " + id));
        entity.setIsActive(!entity.getIsActive());
        entityManager.merge(entity);
        log.info("Trainee activated with id: {}", id);
    }

    public void deactivate(Long id) {
        var entity = findById(id).orElseThrow(()
                -> new IllegalArgumentException("Trainee not found with id: " + id));
        entity.setIsActive(!entity.getIsActive());
        entityManager.merge(entity);
        log.info("Trainee deactivated with id: {}", id);
    }

    @Override
    public void delete(String username) {
        var entity = findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("Trainee not found with username: " + username));
        entityManager.remove(entity);
        log.info("Trainee deleted with username: {}", username);
    }

    @Override
    public List<Trainee> findAll() {
        Query query = entityManager.createQuery(
                "SELECT t FROM Trainee t", Trainee.class);
        return query.getResultList();
    }

    @Override
    public List<String> findUsernamesLike(String likeUsername) {
        return entityManager.createQuery(
                        "select t.userName from Trainee t where t.userName like :username", String.class)
                .setParameter("username", likeUsername)
                .getResultList();
    }
}
