package epam.dao;

import java.time.LocalDate;
import java.util.Objects;

public class TraineeDao extends UserDao {

    private String userId;
    private LocalDate dateOfBirth;
    private String address;

    public TraineeDao() {
    }

    public TraineeDao(String userId, LocalDate dateOfBirth, String address) {
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TraineeDao that = (TraineeDao) o;
        return Objects.equals(userId, that.userId) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, dateOfBirth, address);
    }
}
