package msmgw.heulgkkom.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    @PostConstruct
    public void init() {
        //TimeZone.setDefault(UTC_TIME_ZONE);
    }
}
