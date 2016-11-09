package whl_hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;


public class CreateHbaseTable {

	public static void main(String [] args) throws MasterNotRunningException, ZooKeeperConnectionException, IOException{		
		//创建hbase配置类，该配置类的优先级比hbase-site.xml高
		Configuration conf=HBaseConfiguration.create();
		//配置zookeeper使用的主机
		conf.set("hbase.zookeeper.quorum","master,slave1,slave2");
		//创建Hbase的管理对象
		HBaseAdmin admin=new HBaseAdmin(conf);
		//创建table
		//createTable(admin);
		//添加列簇
		//addColumnFamily(admin);
		//显示hbase表的信息
		//listTableInfo(admin);
		//删除hbase中指定的表
		deleteTable(admin);
	}	
	public static void createTable(HBaseAdmin admin) throws MasterNotRunningException, ZooKeeperConnectionException, IOException{		
		//创建表描述类
		HTableDescriptor tdes=new HTableDescriptor("whl".getBytes());
		//创建列簇描述类
		HColumnDescriptor hdes=new HColumnDescriptor("info".getBytes());
		//添加列簇
		tdes.addFamily(hdes);
		HColumnDescriptor hdes1=new HColumnDescriptor("come".getBytes());
		tdes.addFamily(hdes1);
		admin.createTable(tdes);
		Boolean isaviable=admin.isTableAvailable("whl".getBytes());
		System.out.println("whl table is created and is availables "+isaviable);
	}
	public static void listTableInfo(HBaseAdmin admin) throws IOException{
		TableName[] tablenames=admin.listTableNames();
		for(TableName name:tablenames){
			System.out.println(name);
		}
		HTableDescriptor hdes=admin.getTableDescriptor("whl".getBytes());
		System.out.println(hdes);
	}
	public static void addColumnFamily(HBaseAdmin admin) throws TableNotFoundException, IOException{
		HTableDescriptor tdes=admin.getTableDescriptor("whl".getBytes());
		HColumnDescriptor cdes=new HColumnDescriptor("go".getBytes());
		tdes.addFamily(cdes);
		admin.disableTable("whl".getBytes());
		admin.modifyTable("whl".getBytes(), tdes);
		admin.enableTable("whl".getBytes());
	}
	public static void deleteTable(HBaseAdmin admin) throws IOException{
		if(admin.tableExists("whl".getBytes())){
			admin.disableTable("whl".getBytes());
			admin.deleteTable("whl".getBytes());
			System.out.println("whl table is aviable "+admin.tableExists("whl".getBytes()));
		}
	}
}
