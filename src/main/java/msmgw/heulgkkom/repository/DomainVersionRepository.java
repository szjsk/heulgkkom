package msmgw.heulgkkom.repository;


import java.util.List;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.entity.DomainVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DomainVersionRepository extends JpaRepository<DomainVersion, Long> {
}
