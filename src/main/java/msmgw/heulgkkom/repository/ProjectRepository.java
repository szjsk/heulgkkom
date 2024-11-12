package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByProjectNameAndEnvType(String projectName, String envType);

    List<ProjectEntity> findAllByProjectName(String projectName);

}