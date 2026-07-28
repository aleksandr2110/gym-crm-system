package epam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class GymApplication {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
        /*ApplicationContext context = new AnnotationConfigApplicationContext(GymApplication.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        Trainee trainee = traineeService.select(2L);
        System.out.println("trainee with id " + trainee.toString());

        TrainerService trainerService = context.getBean(TrainerService.class);
        Trainer trainer = trainerService.select(2L);
        System.out.println("trainer with id " + trainer.toString());

        TrainingService trainingService = context.getBean(TrainingService.class); // Impl
        Training training = trainingService.select(2L);
        System.out.println("training with id " + training.toString()); */
    }
}