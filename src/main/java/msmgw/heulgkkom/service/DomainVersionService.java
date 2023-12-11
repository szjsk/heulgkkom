package msmgw.heulgkkom.service;


import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.model.DomainVersionDto;
import msmgw.heulgkkom.model.ServiceManagerDto;
import msmgw.heulgkkom.repository.DomainVersionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainVersionService {

    private final DomainVersionRepository domainVersionRepository;

    public DomainVersion upsertService(DomainVersionDto param){

        DomainVersion data = DomainVersion.builder()
            .domainId(param.getDomainId())
            .versionId(param.getVersionId())
            .requested("todo")
            .build();

        return domainVersionRepository.save(data);
    }

    public DomainVersion getApi(long domainId){
        return domainVersionRepository.findByDomainId(domainId)
            .orElseThrow(() -> new IllegalArgumentException("can not found id"));
    }
}
