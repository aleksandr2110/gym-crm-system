package epam.storage;

import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class TrainingDataLoader implements DataLoader<Long, Training>, ApplicationContextAware {

    private static final Logger logger = Logger.getLogger(TrainingDataLoader.class.getName());
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Map<Long, Training> loadData(InputStream inputStream) {
        Map<Long, Training> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 7) {
                        Map<Long, Trainee> traineeStorage = (Map<Long, Trainee>) applicationContext.getBean("traineeStorage");
                        Map<Long, Trainer> trainerStorage = (Map<Long, Trainer>) applicationContext.getBean("trainerStorage");
                        var training = new Training();
                        training.setId(Long.parseLong(parts[0].trim()));
                        Trainer trainer = trainerStorage.get(Long.parseLong(parts[1].trim()));
                        training.setTrainer(trainer);
                        String traineesLine = parts[2].trim();
                        String[] trainees = traineesLine.split("-");
                        List<Trainee> traineeList = new ArrayList<>();
                        for (String traineeId : trainees) {
                            var trainee = traineeStorage.get(Long.parseLong(traineeId));
                            traineeList.add(trainee);
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

}
