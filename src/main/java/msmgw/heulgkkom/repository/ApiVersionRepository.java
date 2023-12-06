package msmgw.heulgkkom.repository;


import java.util.List;
import msmgw.heulgkkom.entity.ApiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiVersionRepository extends JpaRepository<ApiVersion, Long> {

  List<ApiVersion> findAllByServiceIdOrderByServiceIdDesc(Long serviceId);

}
