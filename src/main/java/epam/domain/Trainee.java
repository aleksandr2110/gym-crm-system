package epam.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Trainee extends User {

    private Long userId;
    private LocalDate dateOfBirth;
    private String address;

    public Trainee() {
    }

    public Trainee(Long userId, LocalDate dateOfBirth, String address) {
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trainee trainee = (Trainee) o;
        return Objects.equals(userId, trainee.userId) && Objects.equals(dateOfBirth, trainee.dateOfBirth) && Objects.equals(address, trainee.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, dateOfBirth, address);
    }
}
