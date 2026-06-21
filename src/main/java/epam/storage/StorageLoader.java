package epam.storage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class StorageLoader implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = Logger.getLogger(StorageLoader.class.getName());

    @Value("${storage.users}")
    private String usersFilePath;

    @Value("${storage.trainers}")
    private String trainersFilePath;

    @Value("${storage.trainees}")
    private String traineesFilePath;

    @Value("${storage.trainings}")
    private String trainingsFilePath;

    @Value("${storage.trainingTypes}")
    private String trainingTypesFilePath;

    private final List<DataLoader<?, ?>> dataLoaders;
    private ApplicationContext applicationContext;
    private Map<String, String> fileMap;

    public StorageLoader(List<DataLoader<?, ?>> dataLoaders) {
        this.dataLoaders = dataLoaders;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        fileMap = new HashMap<>();
        fileMap.put("userStorage", usersFilePath);
        fileMap.put("trainerStorage", trainersFilePath);
        fileMap.put("traineeStorage", traineesFilePath);
        fileMap.put("trainingStorage", trainingsFilePath);
        fileMap.put("trainingTypeStorage", trainingTypesFilePath);

        for (DataLoader<?, ?> loader : dataLoaders) {
            String beanName = loader.getStorageBeanName();
            String filePath = fileMap.get(beanName);
            if (filePath != null && applicationContext.containsBean(beanName)) {
                Map<Object, Object> storage = (Map<Object, Object>) applicationContext.getBean(beanName);
                loadDataFromFile(loader, storage, filePath, beanName);
            }
        }

        logger.info("All storages initialized successfully");
    }

    private <V, ID> void loadDataFromFile(DataLoader<V, ID> loader, Map<Object, Object> storage,
                                  String filePath, String beanName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                logger.warning("File not found: " + filePath);
                return;
            }
            Map<ID, V> loadedData = loader.loadData(is);
            if (loadedData != null && !loadedData.isEmpty()) {
                storage.putAll(loadedData);
                logger.info("Loaded. Count of entities " + loadedData.size() +
                        ", bean name - " + beanName);
            } else {
                logger.warning("Failed to load from "+ filePath);
            }
        } catch (Exception e) {
            logger.warning("Error during loading data for bean - " + beanName);
        }
    }
}
