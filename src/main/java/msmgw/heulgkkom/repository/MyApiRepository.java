package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.MyApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MyApiRepository extends JpaRepository<MyApi, Long> {


}