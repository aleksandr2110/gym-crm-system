package epam.repository;

import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.util.UsernameAndPasswordGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
public class TrainerRepository implements EntityRepository<Trainer, Long> {

    private final EntityManager entityManager;

    public TrainerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Trainer save(Trainer trainer) {
        Trainer createdOptTrainee = null;
        if (trainer == null) {
            log.info("Attempt to save null trainer");
            throw new IllegalArgumentException("Attempt to save null trainer");
        }
        String username = trainer.getUserName();

        if (username == null) {
            trainer.setUserName(UsernameAndPasswordGenerator.createUsername(
                    trainer.getFirstName(),
                    trainer.getLastName()));
            trainer.setPassword(UsernameAndPasswordGenerator.generatePassword());
            trainer.setUserName(checkEqualsUsername(trainer.getUserName()));
            if (trainer.getId() == null) {
                entityManager.persist(trainer);
                log.info("Trainer created with username: {}", trainer.getUserName());
            } else {
                trainer = entityManager.merge(trainer);
                log.info("Trainer updated with username: {}", trainer.getUserName());
            }
        }
        createdOptTrainee = findByUsername(trainer.getUserName());

        return createdOptTrainee;
    }

    @Override
    public Trainer findById(Long id) {
        var entity = entityManager.find(Trainer.class, id);
        if (entity == null) {
            log.warn("Trainer not found with id: {}", id);
        }

        return entity;
    }

    @Override
    public Trainer findByUsername(String userName) {
        try {
            Query query = entityManager.createQuery(
                    "FROM Trainer "  +
                            " t WHERE t.userName = :username", Trainer.class);
            query.setParameter("username", userName);
            return (Trainer) query.getSingleResult();
        } catch (NoResultException e) {
            log.warn("Trainer not found with username: {}", userName);
            return null;
        }
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        var entity = findById(id);
        if (entity == null) {
            log.warn("Trainer not found for password change with id: {}", id);
            throw new IllegalArgumentException("Trainer not found with id: " + id);
        }

        entity.setPassword(newPassword);
        entityManager.merge(entity);
        log.info("Password changed for trainer with id: {}",  id);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        var entity = findByUsername(username);
        if (entity == null) {
            log.warn("Trainer not found for password change: {}", username);
            throw new IllegalArgumentException("Trainer not found with username: " + username);
        }

        entity.setPassword(newPassword);
        entityManager.merge(entity);
        log.info("Password changed for trainer with username {}:", username);
    }

    @Override
    public Trainer updateProfile(Trainer entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void activate(Long id) {
        Optional<Trainer> optionalTrainer = Optional.of(findById(id));
        var entity = optionalTrainer.orElseThrow(()
                -> new IllegalArgumentException("Trainer not found with id: " + id));
        entity.setIsActive(!entity.getIsActive());
        entityManager.merge(entity);
        log.info("Trainer activated with id: {}", id);
    }

    @Override
    public void deactivate(Long id) {
        Optional<Trainer> optionalTrainer = Optional.of(findById(id));
        var entity = optionalTrainer.orElseThrow(()
                -> new IllegalArgumentException("Trainee not found with id: " + id));
        entity.setIsActive(!entity.getIsActive());
        entityManager.merge(entity);
        log.info("Trainer deactivated with id: {}", id);
    }

    @Override
    public boolean authenticate(String userName, String password) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Trainer t WHERE t.userName = :userName AND t.password = :password",
                    Long.class);
            query.setParameter("userName", userName);
            query.setParameter("password", password);
            return (Long) query.getSingleResult() > 0;
        } catch (IllegalArgumentException e) {
            log.error("Error during {} authentication: {}", Trainer.class, e.getMessage());
            return false;
        }
    }

    @Override
    public void delete(String username) {
        var entity = findByUsername(username);
        if (entity == null) {
            log.warn("Trainer not found for deletion with username: {}", username);
            throw new IllegalArgumentException("Trainer not found with username: " + username);
        }

        entityManager.remove(entity);
        log.info("Trainer deleted with username: {}", username);
    }

    @Override
    public List<Trainer> findAll() {
        Query query = entityManager.createQuery(
                "SELECT t FROM Trainer t", Trainer.class);
        return query.getResultList();
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
