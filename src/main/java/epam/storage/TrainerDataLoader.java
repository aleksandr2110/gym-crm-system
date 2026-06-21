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
    public Map<String, TrainerDao> loadData(InputStream inputStream) {
        Map<String, TrainerDao> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 2) {
                        TrainerDao trainer = new TrainerDao();
                        trainer.setUserId(parts[0].trim());
                        trainer.setSpecialization(parts[1].trim());
                        result.put(trainer.getUsername(), trainer);
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
