package epam.service;

import epam.domain.InnerDataTraining;
import epam.domain.Training;
import epam.request.TrainingRequest;

public interface TrainingService {

    Training create(TrainingRequest trainingRequest);
    Training select(InnerDataTraining id);
}
