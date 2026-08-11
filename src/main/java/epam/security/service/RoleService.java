package epam.security.service;


import epam.constants.RoleName;
import epam.domain.entity.Role;
import epam.domain.entity.Trainee;
import epam.domain.entity.Trainer;
import epam.domain.entity.User;
import epam.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public void assignRoleToTrainee(Trainee user, RoleName roleName) {
        log.debug("Assigning role {} to trainee {}", roleName, user.getUsername());

        Role role = roleRepository.findByName(roleName.name())
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.getRoles().add(role);
        log.debug("Role {} assigned to trainee {}", roleName, user.getUsername());
    }

    @Transactional
    public void assignRoleToTrainer(Trainer user, RoleName roleName) {
        log.debug("Assigning role {} to trainer {}", roleName, user.getUsername());

        Role role = roleRepository.findByName(roleName.name())
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.getRoles().add(role);
        log.debug("Role {} assigned to trainer {}", roleName, user.getUsername());
    }
}
