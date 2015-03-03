import com.orientechnologies.orient.core.hook.ODocumentHookAbstract;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class TestHook extends ODocumentHookAbstract {
    @Override
    public DISTRIBUTED_EXECUTION_MODE getDistributedExecutionMode() {
        return DISTRIBUTED_EXECUTION_MODE.BOTH;
    }

    @Override
    public void onRecordAfterRead(ODocument iDocument) {
        System.out.println("onRecordAfterRead called. Document: " + iDocument.toJSON());
    }
}
