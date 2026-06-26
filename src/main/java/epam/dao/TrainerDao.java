package epam.dao;

import java.util.Objects;

public class TrainerDao extends UserDao {

    private Long userId;
    private String specialization;

    public TrainerDao() {
    }

    public TrainerDao(Long userId, String specialization) {
        this.specialization = specialization;
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrainerDao that = (TrainerDao) o;
        return Objects.equals(userId, that.userId) && Objects.equals(specialization, that.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, specialization);
    }
}
