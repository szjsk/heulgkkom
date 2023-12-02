package msmgw.heulgkkom.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.model.CredentialManagerDto;
import msmgw.heulgkkom.repository.ApiDomainRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiDomainService {

    private final ApiDomainRepository apiDomainRepository;

    public ApiDomain insertCredential(CredentialManagerDto param){

        ApiDomain data = ApiDomain.builder()
            .serviceId(param.getServiceId())
            .group(param.getGroup())
            .url(param.getUrl())
            .token(param.getToken())
            .created("todo")
            .build();

        return apiDomainRepository.save(data);
    }

    public ApiDomain updateCredential(CredentialManagerDto param){

        ApiDomain data = apiDomainRepository.findById(param.getCredentialId())
            .orElseThrow(() -> new IllegalArgumentException("can not found id"));

        data.setUrl(param.getUrl());
        data.setToken(param.getToken());
        data.setModifier("todo");

        return apiDomainRepository.save(data);
    }

    public List<ApiDomain> getCredentials(long serviceId){
        return apiDomainRepository.findAllByServiceIdOrderByGroupDesc(serviceId);
    }

}
