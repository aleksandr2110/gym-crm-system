package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.dto.request.TraineeTrainingsRequestDTO;
import epam.domain.dto.request.TrainerTrainingsRequestDTO;
import epam.domain.dto.request.TrainingRequestDTO;
import epam.domain.dto.response.TrainingDTO;
import epam.domain.entity.Training;
import epam.domain.entity.TrainingType;
import epam.domain.entity.TrainingTypeName;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.repository.TrainingTypeRepository;
import epam.service.TrainingService;
import epam.util.DataMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final DataMapper dataMapper;

    public TrainingServiceImpl(TrainingRepository trainingRepository,
                               TraineeRepository traineeRepository,
                               TrainerRepository trainerRepository,
                               TrainingTypeRepository trainingTypeRepository, DataMapper dataMapper) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.dataMapper = dataMapper;
    }

    @ExecutionTime
    @Transactional
    @Override
    public void save(TrainingRequestDTO trainingRequest) {

        var convertedTraining = dataMapper.toTraining(trainingRequest);
        var trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeName.getByName(trainingRequest.getTrainingType().toUpperCase()));
        convertedTraining.setTrainingType(trainingType);

        var training = getTraining(convertedTraining);
        training.setTrainingName(trainingRequest.getTrainingName());
        training.setTrainingDate(trainingRequest.getTrainingDate());
        training.setTrainingDuration(trainingRequest.getTrainingDuration());

        trainingRepository.save(training);
    }

    @Override
    public Training findTrainingById(Long id) {
        return trainingRepository.findTrainingById(id);
    }

    @Override
    public List<Training> getTrainingByTrainingTypeName(String trainingTypeName) {
        return trainingRepository.getTrainingByTrainingTypeName(trainingTypeName);
    }

    @Transactional
    @Override
    public List<TrainingDTO> selectTraineeTrainings(TraineeTrainingsRequestDTO filterRequest) {
        List<Training> trainings = trainingRepository.findTraineeTrainingsByUserNameAndDate(filterRequest.getUsername(),
                filterRequest.getPeriodFrom(), filterRequest.getPeriodTo(), filterRequest.getTrainingType());

        List<TrainingDTO> trainingDTOs = trainings.stream().map(
                training -> {
                    var trainingResponse = new TrainingDTO();
                    trainingResponse.setTrainingName(training.getTrainingName());
                    trainingResponse.setTrainingType(training.getTrainingType().getTrainingTypeName().getName());
                    trainingResponse.setTrainingDate(training.getTrainingDate());
                    trainingResponse.setTrainingDuration(training.getTrainingDuration());
                    trainingResponse.setTrainerName(training.getTrainer().getUsername());
                    return trainingResponse;
                }
        ).toList();
        return trainingDTOs;
    }

    @Transactional
    @Override
    public List<TrainingDTO> selectTrainerTrainings(TrainerTrainingsRequestDTO filterRequest) {
        List<Training> trainings = trainingRepository.findTrainerTrainingsByUserNameAndDate(filterRequest.getUsername(),
                filterRequest.getPeriodFrom(), filterRequest.getPeriodTo());

        List<TrainingDTO> trainingDTOs = trainings.stream().map(
                training -> {
                    var trainingResponse = new TrainingDTO();
                    trainingResponse.setTrainingName(training.getTrainingName());
                    trainingResponse.setTrainingType(training.getTrainingType().getTrainingTypeName().getName());
                    trainingResponse.setTrainingDate(training.getTrainingDate());
                    trainingResponse.setTrainingDuration(training.getTrainingDuration());
                    trainingResponse.setTrainerName(training.getTrainer().getUsername());
                    return trainingResponse;
                }
        ).toList();
        return trainingDTOs;
    }

    private Training getTraining(Training trainingRequest) {
        var training = new Training();
        var trainee = traineeRepository.findByUsername(trainingRequest.getTrainee().getUsername());
        if (trainee.isEmpty()) {
            throw new IllegalArgumentException("Trainee not found with username: "
                    + trainingRequest.getTrainee().getUsername());
        }
        training.setTrainee(trainee.get());

        var trainer = trainerRepository.findByUsername(trainingRequest.getTrainer().getUsername());
        if (trainer.isEmpty()) {
            throw new IllegalArgumentException("Trainer not found with username: "
                    + trainingRequest.getTrainer().getUsername());
        }
        training.setTrainer(trainer.get());
        String trainingName = trainingRequest.getTrainingType().getTrainingTypeName().getName().toUpperCase();
        training.setTrainingType(trainingTypeRepository.findByName(trainingName));

        return training;
    }
}
