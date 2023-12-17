package msmgw.heulgkkom.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.model.ServiceManagerDto;
import msmgw.heulgkkom.repository.ApiServiceRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiServiceService {

    private final ApiServiceRepository apiServiceRepository;
    private static final String FILE_NAME = "service-gradle.txt";

    public ApiService insertService(ServiceManagerDto param, String userId){

        ApiService data = ApiService.builder()
            .serviceName(param.getServiceName())
            .serviceDesc(param.getServiceDesc())
            .serviceGradle(readGradleText(param.getServiceName()))
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

    public ApiService getService(Long serviceId){
        return apiServiceRepository.findById(serviceId)
            .orElseThrow(() -> new IllegalArgumentException("can not found id"));
    }

    public List<ApiService> retrieveServices(){
        return apiServiceRepository.findAll();
    }

    private String readGradleText(String serviceName){
        String gradleTxt = StringUtils.EMPTY;
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                return gradleTxt;
            }
            try (InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
