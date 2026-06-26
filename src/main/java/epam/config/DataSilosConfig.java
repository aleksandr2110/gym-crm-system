package epam.config;

import epam.dao.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSilosConfig {

    @Bean
    public Map<Long, TraineeDao> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, TrainerDao> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, TrainingDao> trainingStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, TrainingTypeDao> trainingTypeStorage() {
        return new HashMap<>();
    }
}
