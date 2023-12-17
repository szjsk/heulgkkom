package msmgw.heulgkkom.repository;


import java.util.Optional;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.model.constant.ServiceStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiServiceRepository extends JpaRepository<ApiService, Long> {

  Optional<ApiService> findByServiceNameAndStatus(String serviceName, ServiceStatusEnum statusEnum);

}
