package msmgw.heulgkkom.service

import spock.lang.Specification

class ApiManagerServiceTest extends Specification {

    private ApiManagerService sut

    def setup() {
        sut = new ApiManagerService()
    }

    def "test12"(){
        given:
            String data = getJsonData();
        when:
        String parse = sut.parse(data);
        println(parse)
        then:
        parse == ""
    }


    def getJsonData(){
        return this.getClass().getResource( '/apidocs.json' ).text
    }
}
