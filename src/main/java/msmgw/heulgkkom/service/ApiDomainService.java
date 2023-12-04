package msmgw.heulgkkom.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.model.DomainManagerDto;
import msmgw.heulgkkom.repository.ApiDomainRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiDomainService {

    private final ApiDomainRepository apiDomainRepository;

    public ApiDomain insertDomain(DomainManagerDto param, String user){

        ApiDomain data = ApiDomain.builder()
            .serviceId(param.getServiceId())
            .group(param.getGroup())
            .url(param.getUrl())
            .token(param.getToken())
            .created(user)
            .build();

        return apiDomainRepository.save(data);
    }

    public ApiDomain updateDomain(DomainManagerDto param, String user){

        ApiDomain data = apiDomainRepository.findById(param.getDomainId())
            .orElseThrow(() -> new IllegalArgumentException("can not found id"));

        data.setUrl(param.getUrl());
        data.setToken(param.getToken());
        data.setModifier(user);

        return apiDomainRepository.save(data);
    }

    public List<ApiDomain> retrieveDomain(long serviceId){
        return apiDomainRepository.findAllByServiceIdOrderByGroupDesc(serviceId);
    }

}
