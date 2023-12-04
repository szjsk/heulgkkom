package msmgw.heulgkkom.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.model.ServiceManagerDto;
import msmgw.heulgkkom.repository.ApiServiceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiServiceService {

    private final ApiServiceRepository apiServiceRepository;

    public ApiService insertService(ServiceManagerDto param, String userId){

        ApiService data = ApiService.builder()
            .serviceName(param.getServiceName())
            .serviceDesc(param.getServiceDesc())
            .status(param.getStatus())
            .created(userId)
            .build();

        return apiServiceRepository.save(data);
    }

    public ApiService updateService(ServiceManagerDto param, String userId){

        ApiService data = apiServiceRepository.findById(param.getServiceId())
            .orElseThrow(() -> new IllegalArgumentException("can not found id"));

        //service name must unique in database
        data.setServiceName(param.getServiceName());
        data.setServiceDesc(param.getServiceDesc());
        data.setStatus(param.getStatus());
        data.setModifier(userId);

        return apiServiceRepository.save(data);
    }

    public ApiService getService(long serviceId){
        return apiServiceRepository.findById(serviceId)
            .orElseThrow(() -> new IllegalArgumentException("can not found id"));
    }

    public List<ApiService> retrieveServices(){
        return apiServiceRepository.findAll();
    }
}
