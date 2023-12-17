package msmgw.heulgkkom.service

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.OpenAPIV3Parser
import msmgw.heulgkkom.model.ApiDto
import msmgw.heulgkkom.repository.ApiComponentRepository
import msmgw.heulgkkom.repository.ApiPathRepository
import msmgw.heulgkkom.repository.ApiVersionRepository
import msmgw.heulgkkom.repository.DomainVersionRepository
import spock.lang.Specification

class ApiManagerServiceTest extends Specification {

    private ApiManagerService sut
    private ApiVersionRepository apiVersionRepository;
    private ApiComponentRepository apiComponentRepository;
    private ApiPathRepository apiPathRepository;
    private DomainVersionRepository domainVersionRepository;

    def setup() {
        apiVersionRepository = Mock(ApiVersionRepository)
        apiComponentRepository = Mock(ApiComponentRepository)
        apiPathRepository = Mock(ApiPathRepository)
        domainVersionRepository = Mock(DomainVersionRepository)

        sut = new ApiManagerService(apiVersionRepository, apiComponentRepository, apiPathRepository, domainVersionRepository)
    }

    def "[getPathAndParameter] print data"(){
        given:
            String data = getJsonData();
            OpenAPI openApi = new OpenAPIV3Parser().readContents(data).getOpenAPI()
        when:
        List<ApiDto> params = sut.getPathAndParameter(openApi)
        then:
        print(params.size())
        !params.isEmpty()
    }


    def getJsonData(){
        return this.getClass().getResource( '/apidocs.json' ).text
    }

}
