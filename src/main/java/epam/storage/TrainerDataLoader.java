package epam.storage;

import epam.dao.TrainerDao;
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
public class TrainerDataLoader implements DataLoader<TrainerDao, String> {

    private static final Logger logger = Logger.getLogger(TrainerDataLoader.class.getName());

    @Override
    public Map<Long, TrainerDao> loadData(InputStream inputStream) {
        Map<Long, TrainerDao> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 7) {
                        TrainerDao trainer = new TrainerDao();
                        trainer.setUserId(Long.parseLong(parts[0].trim()));
                        trainer.setSpecialization(parts[1].trim());
                        trainer.setActive(Boolean.getBoolean(parts[2].trim()));
                        trainer.setFirstName(parts[3].trim());
                        trainer.setLastName(parts[4].trim());
                        trainer.setUsername(parts[5].trim());
                        trainer.setPassword(parts[6].trim());
                        result.put(trainer.getUserId(), trainer);
                    } else {
                        logger.warning("Invalid trainer line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            logger.warning("Error during loading trainers: " + e.getMessage());
        }

        return result;
    }

    @Override
    public String getStorageBeanName() {
        return "trainerStorage";
    }
}
