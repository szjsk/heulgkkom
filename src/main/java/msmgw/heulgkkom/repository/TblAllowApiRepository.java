package msmgw.heulgkkom.repository;


import msmgw.heulgkkom.entity.TblAllowApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblAllowApiRepository extends JpaRepository<TblAllowApi, Long> {

}
