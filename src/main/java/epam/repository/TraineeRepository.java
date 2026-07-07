package epam.repository;


import epam.domain.Trainee;
import epam.util.UsernameAndPasswordGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository("traineeRepository")
//@Qualifier("traineeRepository")
public class TraineeRepository implements EntityRepository<Trainee, Long> {

    private final EntityManager entityManager;

    @Autowired
    public TraineeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Trainee save(Trainee trainee) {
        Trainee createdOptTrainee = null;
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
                log.info("Created trainee with username: {}", trainee.getUserName()); // getUser().
            } else {
                entityManager.merge(trainee); // createdTrainee =
                log.info("Updated trainee with username: {}", trainee.getUserName()); // getUser().
            }
            createdOptTrainee = findByUsername(trainee.getUserName());
        }

        return createdOptTrainee;
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
    public Trainee findByUsername(String userName) {

        try {
            TypedQuery<Trainee> query = entityManager.createQuery("FROM Trainee t WHERE t.userName = :userName", Trainee.class)
                    .setParameter("userName", userName);
            var trainee = query.getSingleResult();

            return trainee;
        } catch (NoResultException e) {
            log.warn("Trainee not found with username: {}", userName);
            return null;
        }
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        var entity = findById(id);
        if (entity == null) {
            log.warn("Trainee not found for password change with id: {}", id);
            throw new IllegalArgumentException("Trainee not found with id: " + id);
        }

        entity.setPassword(newPassword);
        entityManager.merge(entity);
        log.info("Password changed for trainee with id: {}", id);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Trainee entity = findByUsername(username);
        if (entity == null) {
            log.warn("Trainee not found for password change: {}", username);
            throw new IllegalArgumentException("Trainee not found with username: " + username);
        }

        entity.setPassword(newPassword);
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
            entity.setIsActive(true);
        } else {
            entity.setIsActive(false);
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
            entity.setIsActive(false);
        } else {
            entity.setIsActive(true);
        }

        entityManager.merge(entity);
        log.info("Trainee deactivated with id: {}", id);
    }

    @Override
    public boolean authenticate(String userName, String password) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Trainee t WHERE t.userName = :userName AND t.password = :password",
                    Long.class);
            query.setParameter("userName", userName);
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

        while (findByUsername(username) != null) {
            username = baseUsername + identifier;
            identifier++;

        }

        return username;
    }

}
