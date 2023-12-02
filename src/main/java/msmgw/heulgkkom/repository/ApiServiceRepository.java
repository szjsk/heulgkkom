package msmgw.heulgkkom.repository;


import msmgw.heulgkkom.entity.ApiService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiServiceRepository extends JpaRepository<ApiService, Long> {

}
