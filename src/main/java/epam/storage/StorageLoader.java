package epam.storage;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@Component
public class StorageLoader implements ApplicationContextAware, BeanPostProcessor, InitializingBean { //

    private static final Logger logger = Logger.getLogger(StorageLoader.class.getName());

    private final TraineeDataLoader traineeDataLoader = new TraineeDataLoader();
    private final TrainerDataLoader trainerDataLoader = new TrainerDataLoader();
    private final TrainingDataLoader trainingDataLoader = new TrainingDataLoader();
    private final TrainingTypeDataLoader trainingTypeDataLoader = new TrainingTypeDataLoader();

    @Value("${storage.trainers}")
    private String trainersFilePath;

    @Value("${storage.trainees}")
    private String traineesFilePath;

    @Value("${storage.trainings}")
    private String trainingsFilePath;

    @Value("${storage.trainingTypes}")
    private String trainingTypesFilePath;

    private ApplicationContext applicationContext;
    private Map<String, String> fileMap;

    public StorageLoader() {
    }

    /*@Autowired
    public StorageLoader(TraineeDataLoader traineeDataLoader,
                         TrainerDataLoader trainerDataLoader,
                         TrainingDataLoader trainingDataLoader,
                         TrainingTypeDataLoader trainingTypeDataLoader) {
        this.traineeDataLoader = traineeDataLoader;
        this.trainerDataLoader = trainerDataLoader;
        this.trainingDataLoader = trainingDataLoader;
        this.trainingTypeDataLoader = trainingTypeDataLoader;
    }*/

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        fileMap = new HashMap<>();
        fileMap.put("trainerStorage", trainersFilePath);
        fileMap.put("traineeStorage", traineesFilePath);
        fileMap.put("trainingStorage", trainingsFilePath);
        fileMap.put("trainingTypeStorage", trainingTypesFilePath);

        logger.info("All storages initialized successfully");
    }
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException  {
        System.out.println("bean name " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("postProcessAfterInitialization — After Initialization method. Bean name is " + beanName);

        String filePath = fileMap.get(beanName);

        switch (beanName) {
            case "traineeStorage":
                Map<Object, Object> traineeStorage = (Map<Object, Object>) applicationContext.getBean(beanName);
                loadDataFromFile(traineeDataLoader, traineeStorage, filePath, beanName);
                break;
            case "trainerStorage":
                Map<Object, Object> trainerStorage = (Map<Object, Object>) applicationContext.getBean(beanName);
                loadDataFromFile(trainerDataLoader, trainerStorage, filePath, beanName);
                break;
            case "trainingStorage":
                Map<Object, Object> trainingStorage = (Map<Object, Object>) applicationContext.getBean(beanName);
                loadDataFromFile(trainingDataLoader, trainingStorage, filePath, beanName);
                break;
            case "trainingTypeStorage":
                Map<Object, Object> trainingTypeStorage = (Map<Object, Object>) applicationContext.getBean(beanName);
                loadDataFromFile(trainingTypeDataLoader,trainingTypeStorage, filePath, beanName);
                break;
        }
        return bean;
    }

    private <V, ID> void loadDataFromFile(DataLoader<V, ID> loader, Map<Object, Object> storage,
                                  String filePath, String beanName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                logger.warning("File not found: " + filePath);
                return;
            }
            Map<? extends Number, V> loadedData = loader.loadData(is);
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
}
