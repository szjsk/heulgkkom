package msmgw.heulgkkom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project_spec")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ProjectSpecEntity implements Serializable {
    @Id
    @Column(name = "version_id")
    private String versionId;

    @ManyToOne
    @JoinColumn(name = "project_seq")
    private ProjectEntity project;

    @Column(name = "spec_body", columnDefinition = "longtext")
    private String specBody;

}