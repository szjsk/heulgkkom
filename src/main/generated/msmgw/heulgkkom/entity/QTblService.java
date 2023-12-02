package msmgw.heulgkkom.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblService is a Querydsl query type for TblService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblService extends EntityPathBase<ApiService> {

    private static final long serialVersionUID = -80081650L;

    public static final QTblService tblService = new QTblService("tblService");

    public final StringPath created = createString("created");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modifed_at = createDateTime("modifed_at", java.time.LocalDateTime.class);

    public final StringPath modifier = createString("modifier");

    public final StringPath serviceDesc = createString("serviceDesc");

    public final NumberPath<Long> serviceId = createNumber("serviceId", Long.class);

    public final StringPath serviceName = createString("serviceName");

    public final EnumPath<msmgw.heulgkkom.model.constant.ServiceStatusEnum> status = createEnum("status", msmgw.heulgkkom.model.constant.ServiceStatusEnum.class);

    public QTblService(String variable) {
        super(ApiService.class, forVariable(variable));
    }

    public QTblService(Path<? extends ApiService> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblService(PathMetadata metadata) {
        super(ApiService.class, metadata);
    }

}

