package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.MyApi;
import msmgw.heulgkkom.entity.MyComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyComponentRepository extends JpaRepository<MyComponent, Long> {


}