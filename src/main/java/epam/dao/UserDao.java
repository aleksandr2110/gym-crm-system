package epam.dao;

import java.util.Objects;

public class UserDao {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;

    public UserDao() {
    }

    public UserDao(String firstName, String lastName, String username, String password, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDao userDao = (UserDao) o;
        return Objects.equals(firstName, userDao.firstName) && Objects.equals(lastName, userDao.lastName) && Objects.equals(username, userDao.username) && Objects.equals(password, userDao.password) && Objects.equals(isActive, userDao.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, password, isActive);
    }
}
