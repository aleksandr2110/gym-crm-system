package epam.storage;

import epam.dao.TrainingTypeDao;
import epam.domain.Trainer;
import epam.domain.TrainingType;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class TrainingTypeDataLoader implements DataLoader<Long, TrainingType> {

    private static final Logger logger = Logger.getLogger(TrainingTypeDataLoader.class.getName());

    @Override
    public Map<Long, TrainingType> loadData(InputStream inputStream) {
        Map<Long, TrainingType> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 2) {
                        TrainingType trainingType = new TrainingType();
                        trainingType.setUserId(Long.parseLong(parts[0].trim()));
                        trainingType.setTrainingTypeName(parts[1].trim());
                        result.put(trainingType.getUserId(), trainingType);
                    }
                }
            }
        } catch (IOException e) {
            logger.warning("Error during loading training types: " + e.getMessage());
        }

        return result;
    }

}
