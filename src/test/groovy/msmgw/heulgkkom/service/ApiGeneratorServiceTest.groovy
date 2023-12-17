package msmgw.heulgkkom.service

import msmgw.heulgkkom.repository.AllowApiRepository
import msmgw.heulgkkom.repository.ApiComponentRepository
import msmgw.heulgkkom.repository.ApiDomainRepository
import msmgw.heulgkkom.repository.ApiServiceRepository
import spock.lang.Specification

class ApiGeneratorServiceTest extends Specification {

    private ApiDomainRepository apiDomainRepository;
    private ApiServiceRepository apiServiceRepository;
    private ApiComponentRepository apiComponentRepository;
    private AllowApiRepository allowApiRepository;
    private ApiGeneratorService sut;

    def setup(){
        apiDomainRepository  = Mock(ApiDomainRepository)
        apiServiceRepository  = Mock(ApiServiceRepository)
        apiComponentRepository  = Mock(ApiComponentRepository)
        allowApiRepository  = Mock(AllowApiRepository)

        sut =  new ApiGeneratorService(apiDomainRepository, apiServiceRepository, apiComponentRepository, allowApiRepository)
    }


    def "[findString] test"(){
        given:
        String data = getJsonData();
        when:
        List<String> list = sut.findString(data)
        then:
        !list.isEmpty()
    }

    def getJsonData(){
        return this.getClass().getResource( '/apidocs.json' ).text
    }
}
