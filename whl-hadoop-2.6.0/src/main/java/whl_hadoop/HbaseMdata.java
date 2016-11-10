package whl_hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
/*
 * 操作数据类
 */
public class HbaseMdata {

	public static void main(String [] args) throws IOException{
		Configuration conf=HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum","master,slave1,slave2");
		//创建表的操作对象Htable
		HTable mtable=new HTable(conf,"student");
		//添加一条记录
		//addOneRecord(mtable);
		
		//添加多条记录
		//addMulRecord(mtable);
		
		//删除一条记录
		//deleteOneRecord(mtable);
		
		//根据rowkey 查询一条记录单元格数据
		//getOneRecord(mtable);
		
		//根据rowkey查询一条记录数据结构
		//getOneRecordDataInfo(mtable);
		
		//获得多条记录
		//scanData(mtable);
		
		//获得指定时间戳的记录
		getTimeStampData(mtable);
	}
	
	/*
	 * 添加一行记录
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
	/*
	 * 添加多行记录
	 */
	public static void addMulRecord(HTable table) throws IOException{
		List<Put> putList=new ArrayList<Put>();
		//创建一条记录
		Put put1=new Put("jhc".getBytes());//jhc表示row key
		put1.add("address".getBytes(), "city".getBytes(), "pt".getBytes());
		putList.add(put1);
		//创建第二条记录
		Put put2=new Put("hlw".getBytes());
		put2.add("address".getBytes(), "city".getBytes(),"fz".getBytes());
		putList.add(put2);
		table.put(putList);
		table.close();
	}
	/*
	 * 删除一条记录
	 */
	public static void deleteOneRecord(HTable table) throws IOException{
		Delete delete=new Delete("jhc".getBytes());//删除row key为jhc的记录
		table.delete(delete);
		table.close();
	}
	/*
	 * 单条记录的单元格数据查询
	 */
	public static void getOneRecord(HTable table) throws IOException{
		Get get=new Get("whl".getBytes());//获得row key为whl的记录
	    Result result=table.get(get);
	    byte[] value=result.getValue("address".getBytes(),"city".getBytes());
	    System.out.println("whl city is "+Bytes.toString(value));
	    table.close();
	}
	/*
	 * 单条数据结构查询
	 */
	public static void getOneRecordDataInfo(HTable table) throws IOException{
		Get get=new Get("zhangsan".getBytes()); //“zhangsan”为row key
		Result r=table.get(get);
		for(Cell cell:r.rawCells()){
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell)));
			System.out.println("column family为："+new String(CellUtil.cloneFamily(cell)));
			System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell)));
			System.out.println("value 为："+new String(CellUtil.cloneValue(cell)));
			System.out.println("time stamps为："+cell.getTimestamp());
		}
		table.close();
	}
	
	/*
	 * 扫描部分数据,获得多条记录
	 */
	public static void scanData(HTable table) throws IOException  {
		Scan scan=new Scan();
		scan.addColumn("address".getBytes(),"city".getBytes());
		scan.addColumn("address".getBytes(),"province".getBytes());
		scan.addFamily("info".getBytes());
		scan.setStartRow("whl".getBytes());//设置开始的行
		scan.setStopRow("zhangsan".getBytes());//设置结束的行（不包括此行）
		ResultScanner rscan=table.getScanner(scan);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	
	/*
	 * 获得指定时间搓的数据
	 */
	
	public static void getTimeStampData(HTable table) throws IOException{
		Scan s=new Scan();
		s.setTimeRange(NumberUtils.toLong("1111111111"),NumberUtils.toLong("222222222222222"));
		ResultScanner rscan=table.getScanner(s);
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	
}
