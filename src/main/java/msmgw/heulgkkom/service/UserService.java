package msmgw.heulgkkom.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.repository.ProjectRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.*;

import static msmgw.heulgkkom.exception.ServiceExceptionCode.HAS_NOT_PROJECT_AUTH;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;

    public void updateProjectToUser(String projectName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(authentication.getName());

        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        authorities.add(new SimpleGrantedAuthority(projectName));

        jdbcUserDetailsManager.updateUser(User.withUserDetails(userDetails)
                .authorities(authorities.toArray(new GrantedAuthority[0]))
                .build());
    }

    public void registerUser(String username, String password) {
        jdbcUserDetailsManager.createUser(User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .authorities("ROLE_USER")
                .build());
    }

    public void verifyProjectAuthority(UserDetails userDetails, Long projectId) {
        boolean hasAuth = projectRepository.findById(projectId)
                .filter(projectEntity ->
                        userDetails.getAuthorities().stream()
                                .anyMatch(o -> Objects.equals(o.getAuthority(), projectEntity.getProjectName()))
                )
                .isPresent();

        if(!hasAuth) {
            throw HAS_NOT_PROJECT_AUTH.toException();
        }
    }
}
