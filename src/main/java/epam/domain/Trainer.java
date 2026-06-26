package epam.domain;

import java.util.Objects;

public class Trainer extends User {

    private Long userId;
    private String specialization;

    public Trainer() {
    }

    public Trainer(Long userId, String specialization) {
        this.userId = userId;
        this.specialization = specialization;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(userId, trainer.userId) && Objects.equals(specialization, trainer.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, specialization);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "userId='" + userId + '\'' +
                ", specialization='" + specialization + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", Password='" + Password + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
