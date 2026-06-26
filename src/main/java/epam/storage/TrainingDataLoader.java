package epam.storage;

import epam.dao.TraineeDao;
import epam.dao.TrainerDao;
import epam.dao.TrainingDao;
import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.util.TraineeMapper;
import epam.util.TrainerMapper;
import epam.util.TrainingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class TrainingDataLoader implements DataLoader<TrainingDao, String> {

    private static final Logger logger = Logger.getLogger(TrainingDataLoader.class.getName());

    @Autowired
    private Map<Long, TraineeDao> traineeStorage;
    @Autowired
    private Map<Long, TrainerDao> trainerStorage;
    @Autowired
    private TrainingMapper trainingMapper;
    @Autowired
    private TraineeMapper traineeMapper;
    @Autowired
    private TrainerMapper trainerMapper;

    @Override
    public Map<Long, TrainingDao> loadData(InputStream inputStream) {
        Map<Long, TrainingDao> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 7) {
                        TrainingDao training = new TrainingDao();
                        training.setId(Long.parseLong(parts[0].trim()));
                        TrainerDao trainerDao = trainerStorage.get(Long.parseLong(parts[1].trim()));
                        Trainer trainer = trainerMapper.toModel(trainerDao);
                        training.setTrainer(trainer);
                        String traineesLine = parts[2].trim();
                        String[] trainees = traineesLine.split("-");
                        List<Trainee> traineeList = new ArrayList();
                        for (String traineeId : trainees) {
                            var traineeDao = traineeStorage.get(Long.parseLong(traineeId));
                            traineeList.add(traineeMapper.toModelTrainee(traineeDao));
                        }
                        training.setTrainers(traineeList);
                        training.setTrainingName(parts[3].trim());
                        training.setTrainingType(parts[4].trim());
                        training.setTrainingDate(LocalDateTime.parse(parts[5].trim()));
                        training.setTrainingDuration(parts[6].trim());
                        result.put(training.getId(), training);
                    } else {
                        logger.warning("Invalid training line: " + line);
                    }
                }
            }
        } catch (IOException | DateTimeParseException e) {
            logger.warning("Error during loading trainings: " + e.getMessage());
        }

        return result;
    }

    @Override
    public String getStorageBeanName() {
        return  "trainingStorage";
    }
}
