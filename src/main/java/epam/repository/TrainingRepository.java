package epam.repository;

import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.domain.TrainingTypeName;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Repository
public class TrainingRepository {

    private final EntityManager entityManager;

    public TrainingRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Training training) {
        if (training.getId() == null) {
            entityManager.persist(training);
            log.info("Training created: {}", training.getTrainingName());
        } else {
            entityManager.merge(training);
            log.info("Training updated: {}", training.getTrainingName());
        }
    }

    public Training findTrainingById(Long id) {
        var training = entityManager.find(Training.class, id);
        if (training == null) {
            log.warn("Training not found with id: {}", id);
        }

        return training;
    }
    public List<Training> getTrainingByTrainingTypeName(String trainingTypeName) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT t FROM Training t WHERE t.trainingType.trainingTypeName = :trainingTypeName", Training.class);
            query.setParameter("trainingTypeName", TrainingTypeName.getByName(trainingTypeName));
            return (List<Training>) query.getResultList();
        } catch (NoResultException e) {
            log.warn("Trainer not found with training type name: {}", trainingTypeName);
            return null;
        }
    }

    public List<Training> findTraineeTrainingsByUserNameAndDate(String traineeUsername,
                                                                LocalDate fromDate,
                                               LocalDate toDate, String trainingType) {
        return buildTrainingsQuery(training -> {
            Join<Training, Trainee> trainee = training.join("trainee");
            return trainee.get("user").get("userName"); // trainee
        }, traineeUsername, fromDate, toDate, trainingType);
    }

    public List<Training> findTrainerTrainingsByUserNameAndDate(String trainerUsername, LocalDate fromDate, LocalDate toDate) {
        return buildTrainingsQuery(training -> {
            Join<Training, Trainer> trainer = training.join("trainer");
            return trainer.get("user").get("userName");
        }, trainerUsername, fromDate, toDate, null);
    }

    private List<Training> buildTrainingsQuery(Function<Root<Training>, Expression<String>> usernameGetter,
                                               String userName, LocalDate fromDate,
                                               LocalDate toDate, String trainingType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> training = query.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(usernameGetter.apply(training), userName));
        addDatePredicates(cb, training, fromDate, toDate, predicates, trainingType);

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    private void addDatePredicates(CriteriaBuilder cb, Root<Training> training,
                                   LocalDate fromDate, LocalDate toDate, List<Predicate> predicates,
                                   String trainingType) {
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        }
        if (trainingType != null && !trainingType.isBlank()) {
            predicates.add(cb.equal(training.get("trainingType").get("trainingTypeName"), trainingType));
        }
    }
}
