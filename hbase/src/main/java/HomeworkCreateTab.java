
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

public class HomeworkCreateTab {
    public static void main(String[] args) {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "linux121,linux122,linux123");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        Connection conn = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
            //创建Htabledesc描述器，表描述器
            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("user"));
            //指定列族
            tableDescriptor.addFamily(new HColumnDescriptor("friends"));
            admin.createTable(tableDescriptor);
            System.out.println("创建表user_rel成功！");
            admin.close();
            conn.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("获取hbase连接失败！");
        }
    }
}