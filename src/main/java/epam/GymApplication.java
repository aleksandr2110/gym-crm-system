package epam;

import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.service.TraineeService;
import epam.service.TrainerService;
import epam.service.impl.TraineeServiceImpl;
import epam.service.impl.TrainerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class GymApplication {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(GymApplication.class);
        TraineeService traineeService = context.getBean(TraineeServiceImpl.class);
        Trainee trainee = traineeService.select("2");
        System.out.println("trainee with id " + trainee.toString());
        TrainerService trainerService = context.getBean(TrainerServiceImpl.class);
        Trainer trainer = trainerService.select("2");
        System.out.println("trainer with id " + trainer.toString());
    }
}