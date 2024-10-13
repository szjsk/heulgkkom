package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.MyApi;
import msmgw.heulgkkom.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectNameAndEnvType(String projectName, String envType);

}