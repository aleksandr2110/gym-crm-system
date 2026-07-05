package epam.repository;


import epam.domain.Trainee;
import epam.util.UsernameAndPasswordGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@Qualifier("traineeRepository")
public class TraineeRepository implements EntityRepository<Trainee, Long> {

    private final EntityManager entityManager;

    public TraineeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Trainee save(Trainee trainee) {
        Trainee createdTrainee = null;
        if (trainee == null) {
            log.info("Attempt to save null trainee");
            throw new IllegalArgumentException("Attempt to save null trainee");
        }
        String username = trainee.getUserName();

        if (username == null) {
            trainee.setUserName(UsernameAndPasswordGenerator.createUsername(
                    trainee.getFirstName(),
                    trainee.getLastName()));
            trainee.setPassword(UsernameAndPasswordGenerator.generatePassword());
            trainee.setUserName(checkEqualsUsername(trainee.getUserName()));

            if (trainee.getId() == null) {
                entityManager.persist(trainee);
                createdTrainee = findByUsername(trainee.getUserName()).get();
                log.info("Created trainee with username: {}", createdTrainee.getUser().getUserName());
            } else {
                createdTrainee = entityManager.merge(trainee);
                log.info("Updated trainee with username: {}", trainee.getUser().getUserName());
            }
        }

        return createdTrainee;
    }

    @Override
    public Trainee findById(Long id) {

        if (id == null) {
            log.warn("Attempt to select user with null id");
            throw new IllegalArgumentException("Attempt to select user with null id");
        }

        var entity = entityManager.find(Trainee.class, id);
        if (entity == null) {
            log.warn("Trainee not found with id: {}", id);
        }

        return entity;
    }

    @Override
    public Optional<Trainee> findByUsername(String userName) {

        try {
            Query query = entityManager.createQuery(
                    "FROM Trainee t WHERE t.user.userName = :username", Trainee.class);
            query.setParameter("username", userName);
            return (Optional<Trainee>) query.getSingleResult();
        } catch (NoResultException e) {
            log.warn("Trainee not found with username: {}", userName);
            return Optional.empty();
        }
        /*Query query = entityManager.createQuery(
                "SELECT t FROM " + Trainee.class +
                        " t WHERE t.user.username = :username", Trainee.class);
        query.setParameter("username", userName);*/
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        var entity = findById(id);
        if (entity == null) {
            log.warn("Trainee not found for password change with id: {}", id);
            throw new IllegalArgumentException("Trainee not found with id: " + id);
        }

        entity.getUser().setPassword(newPassword);
        entityManager.merge(entity);
        log.info("Password changed for trainee with id: {}", id);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Optional<Trainee> entity = findByUsername(username);
        if (entity.isEmpty()) {
            log.warn("Trainee not found for password change: {}", username);
            throw new IllegalArgumentException("Trainee not found with username: " + username);
        }

        entity.get().getUser().setPassword(newPassword);
        entityManager.merge(entity);
        log.info("Password changed for trainee with user name: {}", username);
    }

    @Override
    public Trainee updateProfile(Trainee entity) {

        return entityManager.merge(entity);
    }

    public void activate(Long id) {
        var entity = findById(id);
        if (entity == null) {
            log.warn("Trainee not found for activation with id: {}", id);
            throw new IllegalArgumentException("Trainee not found with id: " + id);
        }

        if (!entity.getIsActive()) {
            entity.getUser().setIsActive(true);
        } else {
            entity.getUser().setIsActive(false);
        }

        entityManager.merge(entity);
        log.info("Trainee activated with id: {}", id);
    }

    public void deactivate(Long id) {
        var entity = findById(id);
        if (entity == null) {
            log.warn("Trainee not found for deactivation with id: {}", id);
            throw new IllegalArgumentException("Trainee not found with id: " + id);
        }

        if (entity.getIsActive()) {
            entity.getUser().setIsActive(false);
        } else {
            entity.getUser().setIsActive(true);
        }

        entityManager.merge(entity);
        log.info("Trainee deactivated with id: {}", id);
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM " + Trainee.class +
                            " t WHERE t.user.userName = :username AND t.user.password = :password",
                    Long.class);
            query.setParameter("userName", username);
            query.setParameter("password", password);
            return (Long) query.getSingleResult() > 0;
        } catch (IllegalArgumentException e) {
            log.error("Error during authentication: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void delete(String username) {
        var entity = findByUsername(username);
        if (entity == null) {
            log.warn("Trainee not found for deletion with username: {}", username);
            throw new IllegalArgumentException("Trainee not found with username: " + username);
        }

        entityManager.remove(entity);
        var deletedEntity = findByUsername(username);
        if (deletedEntity == null) {
            log.info("Trainee deleted with username: {}", username);
        }
    }

    private String checkEqualsUsername(String baseUsername) {
        Integer identifier = 1;
        String username = baseUsername;

        while (findByUsername(username).isPresent()) {
            username = baseUsername + identifier;
            identifier++;

        }

        return username;
    }

}
