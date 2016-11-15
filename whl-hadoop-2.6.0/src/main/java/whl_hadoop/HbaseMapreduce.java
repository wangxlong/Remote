package whl_hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * mapreduce从hbase读取数据，计算，写入hhbase中
 * @author whl
 *
 */
public class HbaseMapreduce {

	public static void main(String [] args) throws IOException, ClassNotFoundException, InterruptedException{
		Configuration config=HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "master,slave1,slave2");
		
		Job job=new Job(config,"HbaseMapreduce");
		job.setJarByClass(HbaseMapreduce.class);
		
		//new 从hbase获取数据的实例
		Scan scan=new Scan();
		//获取info列簇的height列
		scan.addColumn("info".getBytes(),"height".getBytes());
		//设置缓存500
		scan.setCaching(500);
		//
		scan.setCacheBlocks(false);
		//设置tablemapper job,
		TableMapReduceUtil.initTableMapperJob("student",//从hbase读取数据的数据表
				scan, //获取数据的scan实例
				HeiMapper.class,//mapper处理的实例类
				Text.class,//mapper写出的key类型
				IntWritable.class //maper写出的value类型
				,job);
		//设置tablereuder job
		TableMapReduceUtil.initTableReducerJob("score",//写入score表中，其中score 表在hbase中，该表要存在，否则会报错
				HeiReducer.class,//处理reducer 的类
				job);//因为该reduce直接输出到hbase,而没有输出给别的reducer ，所以没有输出的key类型和输出的value类型定义
		                                                                      
		job.setNumReduceTasks(1);
		boolean b=job.waitForCompletion(true);
		if(!b){
			System.out.println("出错");
		}
	}
	private static class HeiMapper extends TableMapper<Text,IntWritable>{//Text和IntWritable表示mapper输出给ruducer的key类型和value类型

		@Override
		protected void map(ImmutableBytesWritable key, Result value,
				Mapper<ImmutableBytesWritable, Result, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			//super.map(key, value, context);
			for(Cell cell:value.rawCells()){
				String cellname=Bytes.toString(CellUtil.cloneRow(cell));
				if("height".equalsIgnoreCase(Bytes.toString(CellUtil.cloneQualifier(cell)))){
					IntWritable heightwritable=new IntWritable(Integer.parseInt(Bytes.toString(CellUtil.cloneValue(cell))));
					context.write(new Text("average height"), heightwritable);	//输出	
				}
			}
		}
	}
	private static class HeiReducer extends TableReducer<Text, IntWritable, Text>{//Text表示输入的key类型,intWritable表示输出的value类型， Text表示输出的key类型，输出的value类型默认是put或者delete实例

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
				Reducer<Text, IntWritable, Text, Mutation>.Context context) throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			//super.reduce(arg0, arg1, arg2);
			int sum=0;
			int count=0;
			for(IntWritable val:values){
				sum+=val.get();
				count++;
			}
			int average=(int)sum/count;
			Put put=new Put(key.toString().getBytes());
			put.add("average".getBytes(),"average height".getBytes(), String.valueOf(average).getBytes());
			context.write(null, put);//把put值写入hbase的 数据表中
		}	
	}
	
}
