package msmgw.heulgkkom.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblAllowApi is a Querydsl query type for TblAllowApi
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblAllowApi extends EntityPathBase<TblAllowApi> {

    private static final long serialVersionUID = -2048642344L;

    public static final QTblAllowApi tblAllowApi = new QTblAllowApi("tblAllowApi");

    public final NumberPath<Long> api_id = createNumber("api_id", Long.class);

    public final NumberPath<Long> apiId = createNumber("apiId", Long.class);

    public final NumberPath<Long> credentialId = createNumber("credentialId", Long.class);

    public final StringPath reqReason = createString("reqReason");

    public final StringPath requested = createString("requested");

    public final DateTimePath<java.time.LocalDateTime> requestedAt = createDateTime("requestedAt", java.time.LocalDateTime.class);

    public final StringPath requestedContact = createString("requestedContact");

    public final DateTimePath<java.time.LocalDateTime> responseAt = createDateTime("responseAt", java.time.LocalDateTime.class);

    public final StringPath resReason = createString("resReason");

    public final StringPath status = createString("status");

    public QTblAllowApi(String variable) {
        super(TblAllowApi.class, forVariable(variable));
    }

    public QTblAllowApi(Path<? extends TblAllowApi> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblAllowApi(PathMetadata metadata) {
        super(TblAllowApi.class, metadata);
    }

}

