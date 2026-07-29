package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.dto.request.TraineeRequestDTO;
import epam.domain.dto.request.UpdateTraineeRequestDTO;
import epam.domain.dto.response.TraineeProfileDTO;
import epam.domain.dto.response.TrainerInfoDTO;
import epam.domain.entity.Trainee;
import epam.domain.entity.Trainer;
import epam.exception.UnauthorizedException;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.service.TraineeService;
import epam.util.DataMapper;
import epam.util.UsernameAndPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final DataMapper dataMapper;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository, TrainerRepository trainerRepository,
                              DataMapper dataMapper) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.dataMapper = dataMapper;
    }

    @Transactional
    @Override
    public Trainee save(TraineeRequestDTO traineeRequestDTO) {
        var trainee = dataMapper.toTrainee(traineeRequestDTO);

        if (trainee.getUsername() == null) { // getUser().
            setUsername(trainee);
        } else {
            throw new IllegalArgumentException("Attempt to save trainee with username: "
                    + trainee.getUsername());// .getUser().
        }

        var created = traineeRepository.save(trainee);

        return created;
    }

    @Override
    @ExecutionTime
    public Trainee findById(Long id) {
        Trainee trainee = traineeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found with id: " + id));

        return trainee;
    }

    @Transactional
    @Override
    public Trainee findByUsername(String userName) {
        var trainee = traineeRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found with username: " + userName));

        return trainee;
    }

    @Transactional
    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        var trainee = traineeRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found with username: " + username));
        if (!trainee.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        traineeRepository.changePassword(trainee.getUsername(), newPassword);
    }

    @Transactional
    @Override
    public void changePassword(String username, String newPassword) {
        traineeRepository.changePassword(username, newPassword);
    }

    @Transactional
    @Override
    public TraineeProfileDTO updateProfile(UpdateTraineeRequestDTO traineeRequestDTO) {
        Trainee currentTrainee = traineeRepository.findByUsername(traineeRequestDTO.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found with username: " + traineeRequestDTO.getUsername())
        );

        currentTrainee.setFirstName(traineeRequestDTO.getFirstName());
        currentTrainee.setLastName(traineeRequestDTO.getLastName());
        currentTrainee.setAddress(traineeRequestDTO.getAddress());
        currentTrainee.setDateOfBirth(traineeRequestDTO.getDateOfBirth());
        currentTrainee.setActive(traineeRequestDTO.getIsActive());
        var updatedTrainee = traineeRepository.save(currentTrainee);

        TraineeProfileDTO traineeProfileDTO = dataMapper.toProfileTraineeDTO(updatedTrainee);
        traineeProfileDTO.setIsActive(updatedTrainee.isActive());

        return traineeProfileDTO;
    }

    @Transactional
    @Override
    public void activateDeactivateTrainee(String username, boolean isActive) {
        var entity = traineeRepository.findByUsername(username).orElseThrow(()
                -> new IllegalArgumentException("Trainee not found with username : " + username));;

        if (isActive) {
            traineeRepository.activate(entity.getId());
        } else {
            traineeRepository.deactivate(entity.getId());
        }
    }

    @Transactional
    @Override
    public Trainee authenticateTrainee(String username, String password) throws IllegalArgumentException {
        var entity = traineeRepository.findByUsername(username).orElseThrow(()
                -> new IllegalArgumentException("Trainee not found with username: " + username));
        if (!entity.getPassword().equals(password)) {
            throw new UnauthorizedException("User is not authenticated: " + username);
        }
        return entity;
    }

    @Transactional
    @Override
    public void deleteProfile(String username) {
        traineeRepository.delete(username);
    }

    @Override
    @Transactional
    public List<Trainer> updateTrainersList(String traineeUsername, List<String> trainerUsernames) {
        var trainee = traineeRepository.findByUsername(traineeUsername).orElseThrow(()
                -> new IllegalArgumentException("Trainee not found with username: " + traineeUsername));

        List<Trainer> newTrainers = trainerUsernames.stream()
                .map(username -> {
                    Trainer trainer = trainerRepository.findByUsername(username).orElseThrow(()
                            -> new IllegalArgumentException("Trainer not found with username: " + username));
                    return trainer;
                })
                .toList();

        trainee.setTrainers(new ArrayList<>(newTrainers));
        traineeRepository.save(trainee);

        return newTrainers;
    }

    private void setUsername(Trainee trainee) {
        trainee.setUsername(UsernameAndPasswordGenerator.createUsername(
                trainee.getFirstName(),
                trainee.getLastName()));
        trainee.setPassword(UsernameAndPasswordGenerator.generatePassword());
        List<String> usernameDuplicates = traineeRepository.findUsernamesLike(trainee.getFirstName() + "%");
        trainee.setUsername(trainee.getUsername() + (usernameDuplicates.size() == 0 ? "" : usernameDuplicates.size()));
    }

}
