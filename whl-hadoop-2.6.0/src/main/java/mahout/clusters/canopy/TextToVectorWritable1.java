package mahout.clusters.canopy;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class TextToVectorWritable1 extends AbstractJob{

	public static void main(String [] args){
		Configuration conf=new Configuration();
		try {
			ToolRunner.run(conf, new TextToVectorWritable1(), args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 实现AbstractJob中run函数
	 * (non-Javadoc)
	 * @see org.apache.hadoop.util.Tool#run(java.lang.String[])
	 */
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		addInputOption();//添加输入目录
		addOutputOption();//添加输出目录
		addOption("inputfile","ii","input data dirctory","input");
		/*
		 * 也可以自定义添加参数的形式，如：
		 * addOption("inputfile","ii","input data dirctory","input");//参数的顺序为name,shortname,description,defaultvalue
		 * 则在命令行是 -ii后面的值表示name 为inputfile参数选项的值，
		 * 可以用：String inputd=getOption("inputfile");获取该值
		 */
		//判断解析出来的参数是否为空，为空则说明输出错误，则停止输入
		Map<String,List<String>> mapArg=parseArguments(args);
		if(mapArg==null){
			System.out.print("解析结果无参数");
		}
		Path input=getInputPath();
		Path output=getOutputPath();
		//获得conf
		Configuration conf=getConf();			
		System.out.println("输入路径："+input);
		System.out.println("输出路径："+output);
		//获得output path对象的文件操作对象 filesystem,如何存在该输出目录，则删除输出目录，否则报错
		FileSystem fs=output.getFileSystem(conf);
		if(fs.exists(output)){
			System.out.println("存在该输出目录--删除");
			fs.delete(output);
			System.out.println("删除输出目录成功");
		}
			
		//set Job INFO		
		Job job=new Job(conf, "Text2VectorWritable");
		
		//设置mapper
		job.setMapperClass(Text2VectorWritableMapper.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(VectorWritable.class);
		//设置reducer
		job.setReducerClass(Text2VectorWritableReducer.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(VectorWritable.class);
		
		//设置输入输出的文件格式
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		TextInputFormat.setInputPaths(job, input);
		SequenceFileOutputFormat.setOutputPath(job, output);
		job.setJarByClass(TextToVectorWritable1.class);
		if(!job.waitForCompletion(true)){
			System.out.println("运行错误");
		}
		return 0;
	}
	/**
	 * mapper静态类
	 * @author whl
	 *
	 */
	public static class Text2VectorWritableMapper extends Mapper<LongWritable, Text, LongWritable, VectorWritable>{

		@Override
		protected void cleanup(Mapper<LongWritable, Text, LongWritable, VectorWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			super.cleanup(context);
			System.out.println("mapper 结束");
		}

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, LongWritable, VectorWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String [] str=value.toString().split(",");
			Vector ve=new RandomAccessSparseVector(str.length);
			for(int i=0;i<str.length;i++){
				ve.set(i, Double.parseDouble(str[i]));
			}
			VectorWritable vw=new VectorWritable(ve);
			context.write(key, vw);
		}

		@Override
		protected void setup(Mapper<LongWritable, Text, LongWritable, VectorWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			System.out.println("mapper开始");
			super.setup(context);
		}
		
	}
	
	public static class Text2VectorWritableReducer extends Reducer<LongWritable,VectorWritable, LongWritable,VectorWritable>{

		@Override
		protected void cleanup(Reducer<LongWritable, VectorWritable, LongWritable, VectorWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			//super.cleanup(context);
			System.out.println("reducer结束");
		}

		@Override
		protected void reduce(LongWritable key, Iterable<VectorWritable> values,
				Reducer<LongWritable, VectorWritable, LongWritable, VectorWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			for(VectorWritable vw:values){
				context.write(key, vw);
			}
		}

		@Override
		protected void setup(Reducer<LongWritable, VectorWritable, LongWritable, VectorWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			//super.setup(context);
			System.out.println("reducer开始");
		}
		
	}
}
