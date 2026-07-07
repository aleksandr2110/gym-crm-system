package epam.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;

    @Column(name = "first_name", nullable = false)
    protected String firstName;

    @Column(name = "last_name", nullable = false)
    protected String lastName;

    @Column(name = "username", nullable = false)
    protected String userName;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "is_active", nullable = false)
    protected Boolean isActive;

    /*@OneToOne(mappedBy = "user")
    protected Trainee trainee;

    @OneToOne(mappedBy = "user")
    protected Trainer trainer;*/

}
