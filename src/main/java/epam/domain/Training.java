package epam.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Training {

    private InnerDataTraining innerDataTraining;
    private String trainingType;
    private LocalDateTime trainingDate;
    private String trainingDuration;

    public Training() {
    }

    public Training(InnerDataTraining innerDataTraining, String trainingType,
                    LocalDateTime trainingDate, String trainingDuration) {
        this.innerDataTraining = innerDataTraining;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

}
