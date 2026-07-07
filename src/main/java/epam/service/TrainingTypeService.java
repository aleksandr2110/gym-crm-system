package epam.service;

import epam.domain.TrainingType;

import java.util.List;

public interface TrainingTypeService {

    void saveTrainingType(List<TrainingType> trainingList);
    TrainingType findByName(String name);
    TrainingType findById(Long id);
    List<TrainingType> findAll();
}
