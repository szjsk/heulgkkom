package msmgw.heulgkkom.service;


import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.model.ServiceManagerDto;
import msmgw.heulgkkom.repository.ApiServiceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiServiceService {

    private final ApiServiceRepository apiServiceRepository;

    public ApiService insertService(ServiceManagerDto param){

        ApiService data = ApiService.builder()
            .serviceName(param.getServiceName())
            .status(param.getStatus())
            .created("todo")
            .build();

        return apiServiceRepository.save(data);
    }

    public ApiService updateService(ServiceManagerDto param){

        ApiService data = apiServiceRepository.findById(param.getServiceId())
            .orElseThrow(() -> new IllegalArgumentException("can not found id"));

        data.setServiceName(param.getServiceName());
        data.setServiceDesc(param.getServiceDesc());
        data.setStatus(param.getStatus());
        data.setModifier("todo");

        return apiServiceRepository.save(data);
    }

    public ApiService getService(long serviceId){
        return apiServiceRepository.findById(serviceId)
            .orElseThrow(() -> new IllegalArgumentException("can not found id"));
    }
}
