package msmgw.heulgkkom.repository;


import java.util.List;
import java.util.Optional;
import msmgw.heulgkkom.entity.ApiDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiDomainRepository extends JpaRepository<ApiDomain, Long> {

  List<ApiDomain> findAllByServiceIdOrderByGroupDesc(Long serviceId);

  List<ApiDomain> findAllByGroup(String group);

  Optional<ApiDomain> findByServiceIdAndGroup(Long serviceId, String group);
}
