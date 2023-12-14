package msmgw.heulgkkom.repository;


import java.util.List;
import msmgw.heulgkkom.entity.ApiPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiPathRepository extends JpaRepository<ApiPath, String> {

  List<ApiPath> findAllByVersionIdOrderByPathAscMethodAsc(Long versionId);

}
