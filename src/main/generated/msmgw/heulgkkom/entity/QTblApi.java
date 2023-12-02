package msmgw.heulgkkom.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblApi is a Querydsl query type for TblApi
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblApi extends EntityPathBase<ApiPath> {

    private static final long serialVersionUID = 222483699L;

    public static final QTblApi tblApi = new QTblApi("tblApi");

    public final NumberPath<Long> api_id = createNumber("api_id", Long.class);

    public final StringPath created = createString("created");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> credentialId = createNumber("credentialId", Long.class);

    public final StringPath method = createString("method");

    public final StringPath operationId = createString("operationId");

    public final StringPath parameters = createString("parameters");

    public final StringPath path = createString("path");

    public final StringPath reponses = createString("reponses");

    public final StringPath tags = createString("tags");

    public final StringPath version = createString("version");

    public QTblApi(String variable) {
        super(ApiPath.class, forVariable(variable));
    }

    public QTblApi(Path<? extends ApiPath> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblApi(PathMetadata metadata) {
        super(ApiPath.class, metadata);
    }

}

