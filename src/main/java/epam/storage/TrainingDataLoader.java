package epam.storage;

import epam.dao.InnerDataTrainingDao;
import epam.dao.TrainingDao;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class TrainingDataLoader implements DataLoader<TrainingDao, InnerDataTrainingDao> {

    private static final Logger logger = Logger.getLogger(TrainingDataLoader.class.getName());

    @Override
    public Map<InnerDataTrainingDao, TrainingDao> loadData(InputStream inputStream) {
        Map<InnerDataTrainingDao, TrainingDao> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 6) {
                        InnerDataTrainingDao inner = new InnerDataTrainingDao(
                                parts[0].trim(),
                                parts[1].trim(),
                                parts[2].trim()
                        );

                        TrainingDao training = new TrainingDao();
                        training.setInnerDataTraining(inner);
                        training.setTrainingType(parts[3].trim());
                        training.setTrainingDate(LocalDateTime.parse(parts[4].trim()));
                        training.setTrainingDuration(parts[5].trim());
                        result.put(inner, training);
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
