package msmgw.heulgkkom.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblCredential is a Querydsl query type for TblCredential
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblCredential extends EntityPathBase<ApiDomain> {

    private static final long serialVersionUID = -964143266L;

    public static final QTblCredential tblCredential = new QTblCredential("tblCredential");

    public final StringPath created = createString("created");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> credentialId = createNumber("credentialId", Long.class);

    public final StringPath group = createString("group");

    public final DateTimePath<java.time.LocalDateTime> modifedAt = createDateTime("modifedAt", java.time.LocalDateTime.class);

    public final StringPath modifier = createString("modifier");

    public final NumberPath<Long> serviceId = createNumber("serviceId", Long.class);

    public final StringPath token = createString("token");

    public final StringPath url = createString("url");

    public QTblCredential(String variable) {
        super(ApiDomain.class, forVariable(variable));
    }

    public QTblCredential(Path<? extends ApiDomain> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblCredential(PathMetadata metadata) {
        super(ApiDomain.class, metadata);
    }

}

