package whl_hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;

public class HbaseMdata {

	public static void main(String [] args) throws IOException{
		Configuration conf=HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum","master,slave1,slave2");
		//创建表的操作对象Htable
		HTable mtable=new HTable(conf,"student");
		//添加一条记录
		addOneRecord(mtable);
	}
	
	/*
	 * 添加一条记录
	 */
	public static void addOneRecord(HTable table) throws IOException{
		//创建put对象，whl为row key
		Put put=new Put("whl".getBytes()); 
		//添加数据，“address”为列簇，“city”为列，“hangzhou”为值
		put.add("address".getBytes(),"city".getBytes(), "hangzhou".getBytes());
		put.add("address".getBytes(), "province".getBytes(), "zhejiang".getBytes());
		table.put(put);
		table.close();
		System.out.println("成功添加一条记录！");
	}
}
