
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableWrapper;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Deletecoprocessor extends BaseRegionObserver {
    Logger logger = LoggerFactory.getLogger(Deletecoprocessor.class);

    @Override
    public void postDelete(ObserverContext<RegionCoprocessorEnvironment> e, final Delete delete, WALEdit edit, Durability durability) throws IOException {
        HTableWrapper userRel = (HTableWrapper) e.getEnvironment().getTable(TableName.valueOf("user_rel"));
        //获取删除的行rowkey
        byte[] row = delete.getRow();
        //获取到firends下所有的cell
        List<Cell> friends = delete.getFamilyCellMap().get(Bytes.toBytes("friends"));
        for (Cell friend : friends) {
            byte[] bytes = CellUtil.cloneQualifier(friend);
            Delete delete1 = new Delete(bytes);
            delete1.addColumn(Bytes.toBytes("friends"), row);
            userRel.delete(delete1);
        }
        userRel.flushCommits();
        userRel.close();
    }
}