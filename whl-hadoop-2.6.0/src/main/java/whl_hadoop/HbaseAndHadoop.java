package whl_hadoop;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/*
 * hadoop读取hbase数据，mapreduce处理，然后写入HDFS中
 */

public class HbaseAndHadoop{
	public static void main(String [] args) throws IOException, ClassNotFoundException, InterruptedException{
		
		Configuration config=HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "master,slave1,slave2");
		
		Scan scan=new Scan();
		scan.addColumn("info".getBytes(), "height".getBytes());
		scan.setCaching(500);
		scan.setCacheBlocks(false);
		
		Job job=new Job(config,"tablemapreducer");
		
		job.setJarByClass(HbaseAndHadoop.class);
		
		//设置从hbase取数据的mapper
		TableMapReduceUtil.initTableMapperJob("student",scan, StuMapper.class, Text.class, IntWritable.class, job);
		
		job.setReducerClass(StuReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
	
		job.setOutputFormatClass(TextOutputFormat.class);	//输出hdfs的数据格式
		FileOutputFormat.setOutputPath(job, new Path(args[0]));//输出路径
		job.setNumReduceTasks(1);
		boolean b=job.waitForCompletion(true);
		if(!b){
			System.out.println("运行失败");
		}
	}
	
	private static class StuMapper extends TableMapper<Text, IntWritable>{

		@Override
		protected void map(ImmutableBytesWritable key, Result value,
				Mapper<ImmutableBytesWritable, Result, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			//super.map(key, value, context);
			Text writeablename=null;
			IntWritable writeablehight=null;
			byte []bytename=null;
			byte []bytehight=null;
			for(Cell cell:value.rawCells()){
				bytename=CellUtil.cloneRow(cell);
				bytehight=CellUtil.cloneValue(cell);
				if("height".equalsIgnoreCase(Bytes.toString(CellUtil.cloneQualifier(cell)))){
					if(bytehight!=null){
						writeablename=new Text("average height");
						writeablehight=new IntWritable(Integer.parseInt(Bytes.toString(bytehight)));
						context.write(writeablename, writeablehight);
					}
					break;
				}
			}
		}	
	}
	
	private static class StuReducer extends Reducer<Text, IntWritable, Text,IntWritable>{

		@Override
		protected void reduce(Text key, Iterable<IntWritable> value,
				Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			//super.reduce(arg0, arg1, arg2);
			int sum=0,count=0;
			Iterator<IntWritable> iterator=value.iterator();
			while(iterator.hasNext()){
				sum+=iterator.next().get();
				count++;
			}
			int average=(int)sum/count;
			context.write(new Text("hight average"), new IntWritable(average));//输出到hdfs
		}		
	}
	
	
}

