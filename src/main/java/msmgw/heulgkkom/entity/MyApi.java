package msmgw.heulgkkom.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "my_api")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MyApi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_id")
    private Long apiId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "path")
    private String path;

    @Column(name = "method")
    @Enumerated(EnumType.STRING)
    private HttpMethodEnum method;

    @Column(name = "spec_body")
    private String specBody;

    @Column(name = "parameter")
    private String parameter;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    @Column(name = "version")
    private String version;

}