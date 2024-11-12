package msmgw.heulgkkom.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;

@Entity
@Table(name = "project_api")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ProjectApiEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_seq")
    private Long apiSeq;

    @ManyToOne
    @JoinColumn(name = "project_seq")
    private ProjectEntity project;

    @Column(name = "version_id", length = 500)
    private String versionId;

    @Column(name = "path", length = 2000, nullable = false)
    private String path;

    @Column(name = "method", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethodEnum method;

    @Column(name = "parameter", columnDefinition = "longtext")
    private String parameter;

    @Column(name = "request", columnDefinition = "longtext")
    private String request;

    @Column(name = "response", columnDefinition = "longtext")
    private String response;

}