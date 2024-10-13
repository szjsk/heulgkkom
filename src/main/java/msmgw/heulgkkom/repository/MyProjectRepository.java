package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.MyApi;
import msmgw.heulgkkom.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyProjectRepository extends JpaRepository<Project, Long> {


}