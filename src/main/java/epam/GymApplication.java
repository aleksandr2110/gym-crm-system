package epam;

import epam.application.FacadeGymCrmSystem;
import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.request.TraineeDTO;
import epam.service.TraineeService;
import epam.service.TrainerService;
import epam.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@ComponentScan
public class GymApplication {

    @Autowired
    private ApplicationContext context;


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(GymApplication.class);
        FacadeGymCrmSystem facadeGymCrmSystem = context.getBean(FacadeGymCrmSystem.class);
        var traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("Anton");
        traineeDTO.setLastName("Artuh");
        traineeDTO.setAddress("Ukraine sokol st 89");
        traineeDTO.setDateOfBirth(LocalDate.of(1986, 11, 4));
        traineeDTO.setActive(Boolean.TRUE);
        facadeGymCrmSystem.createTrainee(traineeDTO);


        /*TraineeService traineeService = context.getBean(TraineeService.class);
        Trainee trainee = traineeService.findById(2L);
        System.out.println("trainee with id " + trainee.toString());

        TrainerService trainerService = context.getBean(TrainerService.class);
        Trainer trainer = trainerService.findById(2L);
        System.out.println("trainer with id " + trainer.toString());*/

        /*TrainingService trainingService = context.getBean(TrainingService.class); // Impl
        Training training = trainingService.findById(2L);
        System.out.println("training with id " + training.toString()); */
    }
}