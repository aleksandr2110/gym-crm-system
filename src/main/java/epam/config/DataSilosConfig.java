package epam.config;

import epam.dao.*;
import epam.domain.InnerDataTraining;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSilosConfig {

    @Bean
    public Map<String, UserDao> userStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, TrainerDao> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, TraineeDao> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<InnerDataTraining, TrainingDao> trainingStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, TrainingTypeDao> trainingTypeStorage() {
        return new HashMap<>();
    }
}
