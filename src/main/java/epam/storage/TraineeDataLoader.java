package epam.storage;

import epam.domain.Trainee;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class TraineeDataLoader implements DataLoader<Long, Trainee> {

    private static final Logger logger = Logger.getLogger(TraineeDataLoader.class.getName());

    @Override
    public Map<Long, Trainee> loadData(InputStream inputStream) {
        Map<Long, Trainee> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 8) {
                        Trainee trainee = new Trainee();
                        trainee.setUserId(Long.parseLong(parts[0].trim()));
                        trainee.setDateOfBirth(LocalDate.parse(parts[1].trim()));
                        trainee.setAddress(parts[2].trim());
                        trainee.setActive(Boolean.getBoolean(parts[3].trim()));
                        trainee.setFirstName(parts[4].trim());
                        trainee.setLastName(parts[5].trim());
                        trainee.setUserName(parts[6].trim());
                        trainee.setPassword(parts[7].trim());
                        result.put(trainee.getUserId(), trainee);
                    } else {
                        logger.warning("Invalid trainee line: {} " + line);
                    }
                }
            }
        } catch (IOException | DateTimeParseException e) {
            logger.warning("Error during loading trainees: {} " + e.getMessage());
        }

        return result;
    }

}
