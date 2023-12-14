package msmgw.heulgkkom.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.model.DomainGroupDto;
import msmgw.heulgkkom.model.DomainManagerDto;
import msmgw.heulgkkom.repository.ApiDomainRepository;
import msmgw.heulgkkom.repository.ApiServiceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiDomainService {

    private final ApiDomainRepository apiDomainRepository;
    private final ApiServiceRepository apiServiceRepository;

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

    public List<ApiDomain> retrieveDomain(Long serviceId){
        return apiDomainRepository.findAllByServiceIdOrderByGroupDesc(serviceId);
    }

    public List<DomainGroupDto> retrieveSameDomainGroup(Long domainId){
        ApiDomain domain = apiDomainRepository.findById(domainId)
            .orElseThrow(() -> new IllegalArgumentException("can not find domain"));

        Map<Long, ApiDomain> domainByService = apiDomainRepository.findAllByGroup(domain.getGroup()).stream()
            .filter(o-> !Objects.equals(o.getServiceId(), domain.getServiceId()))
            .collect(Collectors.toMap(ApiDomain::getServiceId, Function.identity(), (a,b)->a));

        return apiServiceRepository.findAllById(domainByService.keySet())
            .stream()
            .map(o->{
                ApiDomain d = domainByService.get(o.getServiceId());
                return DomainGroupDto.of(o.getServiceId(), d.getDomainId(), o.getServiceName(), d.getGroup());
            }).toList();
    }



}
