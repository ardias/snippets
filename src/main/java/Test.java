import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

public class Test {

    public static void main(String...args) throws IOException {

        Test t = new Test();
        t.run();

        System.exit(0);
    }

    private ODatabaseDocumentTx db;

    private final String className = "Data";
    private final String FIELD_ID = "ID";
    private final String FIELD_STATUS = "STATUS";
    private final String STATUS = "processed";

    private void run() {

        db = new ODatabaseDocumentTx("memory:temp");
        db.create();
        registerClass(className);
        db.registerHook(new TestHook());

        String id = UUID.randomUUID().toString();
        ODocument first = new ODocument(className);
        first.field(FIELD_ID, id);
        first.field(FIELD_STATUS, STATUS);
        db.save(first);

        System.out.println("WITHOUT INDEX: onRecordAfterRead will be called twice");
        ODocument found = queryByStatus(STATUS);

        System.out.println("WITH INDEX: onRecordAfterRead will be called only once");
        found = queryById(id);
    }

    private ODocument queryByStatus(String status) {
        String sql = "SELECT FROM " + className + " WHERE " + FIELD_STATUS + " = '" + status + "'";
        OSQLSynchQuery<ODocument> query =
            new OSQLSynchQuery<ODocument>(sql.toString());
        List<ODocument> results  = db.query(query);
        return results.get(0);
    }

    private ODocument queryById(String id) {
        String sql = "SELECT FROM " + className + " WHERE " + FIELD_ID + " = '" + id + "'";
        OSQLSynchQuery<ODocument> query =
            new OSQLSynchQuery<ODocument>(sql.toString());
        List<ODocument> results  = db.query(query);
        return results.get(0);
    }

    private void registerClass(String className) {
        OClass aClass =
            db.getMetadata().getSchema()
              .createClass(className);
        aClass.createProperty(FIELD_ID, OType.STRING);
        aClass.createProperty(FIELD_STATUS, OType.STRING);

        aClass.createIndex("IDX", OClass.INDEX_TYPE.NOTUNIQUE, FIELD_ID);
    }
}
