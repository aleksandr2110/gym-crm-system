package epam.storage;

import epam.dao.TraineeDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class TraineeDataLoader implements DataLoader<TraineeDao, String>, BeanPostProcessor, ApplicationContextAware {

    private static final Logger logger = Logger.getLogger(TraineeDataLoader.class.getName());

    private ApplicationContext applicationContext;
    //private Loader loader;

    @Value("${storage.trainees}")
    private String traineesFilePath;

//    @Autowired
//    public void setLoader(Loader loader) {
//        this.loader = loader;
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /*@Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("postProcessAfterInitialization — After Initialization method. Bean name is " + beanName);

        if (beanName.equals("traineeStorage")) {
            loader.loadDataPostProcessAfterInitialization(beanName);
            Map<Long, TraineeDao> storage = (Map<Long, TraineeDao>) applicationContext.getBean(beanName);
            loadDataFromFile(storage, traineesFilePath, beanName);
        }

        return bean;
    }*/

    public <V, ID> void loadDataFromFile(Map<Long, TraineeDao> storage,
                                         String filePath, String beanName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                logger.warning("File not found: " + filePath);
                return;
            }
            //Map<? extends Number, TraineeDao> loadedData = loader.loadData(is);
            Map<Long, TraineeDao> loadedData = loadData(is);
            if (loadedData != null && !loadedData.isEmpty()) {
                storage.putAll(loadedData);
                logger.info("Loaded. Count of entities " + loadedData.size() +
                        ", bean name - " + beanName);
            } else {
                logger.warning("Failed to load from " + filePath);
            }
        } catch (Exception e) {
            logger.warning("Error during loading data for bean - " + beanName);
        }
    }

    @Override
    public Map<Long, TraineeDao> loadData(InputStream inputStream) {
        Map<Long, TraineeDao> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 8) {
                        TraineeDao trainee = new TraineeDao();
                        trainee.setUserId(Long.parseLong(parts[0].trim()));
                        trainee.setDateOfBirth(LocalDate.parse(parts[1].trim()));
                        trainee.setAddress(parts[2].trim());
                        trainee.setActive(Boolean.getBoolean(parts[3].trim()));
                        trainee.setFirstName(parts[4].trim());
                        trainee.setLastName(parts[5].trim());
                        trainee.setUsername(parts[6].trim());
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

    @Override
    public String getStorageBeanName() {
        return "traineeStorage";
    }

}
