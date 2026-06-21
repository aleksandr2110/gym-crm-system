package epam.storage;

import epam.dao.UserDao;
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
public class UserDataLoader implements DataLoader<UserDao, String> {

    private static final Logger logger = Logger.getLogger(UserDataLoader.class.getName());

    @Override
    public Map<String, UserDao> loadData(InputStream inputStream) {
        Map<String, UserDao> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 5) {
                        UserDao user = new UserDao();
                        user.setFirstName(parts[0].trim());
                        user.setLastName(parts[1].trim());
                        user.setUsername(parts[2].trim());
                        user.setPassword(parts[3].trim());
                        user.setActive(Boolean.parseBoolean(parts[4].trim()));
                        result.put(user.getUsername(), user);
                    } else {
                        logger.warning("Invalid user line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            logger.warning("Error during loading users: " + e.getMessage());
        }

        return result;
    }

    @Override
    public String getStorageBeanName() {
        return "userStorage";
    }
}
