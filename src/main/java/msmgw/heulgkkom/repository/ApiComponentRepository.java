package msmgw.heulgkkom.repository;


import java.util.List;
import java.util.Set;
import msmgw.heulgkkom.entity.ApiComponent;
import msmgw.heulgkkom.entity.ApiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiComponentRepository extends JpaRepository<ApiComponent, Long> {

  List<ApiComponent> findAllByVersionIdIn(Set<Long> versionId);

}
