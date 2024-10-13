package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.MyApi;
import msmgw.heulgkkom.entity.PermittedProjectApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPermittedProjectApiRepository extends JpaRepository<PermittedProjectApi, Long> {


}