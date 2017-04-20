package mahout.clusters.canopy;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.canopy.CanopyClusterer;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.conversion.InputDriver;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.ClassUtils;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.commandline.DefaultOptionCreator;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.utils.clustering.ClusterDumper;

/**
 * canopy聚类算法的使用
 * 必须要指定的参数有：数据输入路径，输出路径，距离计算类，T1，T2
 * T1表示外圈距离，T2表示内圈距离
 * @author whl
 *
 */
public class CanopyCluster extends AbstractJob{

	public static final String filedir="hdfs://192.168.95.131:9000/user/mahout/";
	public static final String inputSequenceData="hdfs://192.168.95.131:9000/user/mahout/output/canopy/part-r-00000";
	public static final String clusterOutputSequenceData="hdfs://192.168.95.131:9000/user/mahout/output/canopyClustered/";
	
	
	public static void main(String [] args) throws Exception{
		if(args.length>0){
			ToolRunner.run(new Configuration(), new CanopyCluster(), args);
		}else{
			Path output = new Path(clusterOutputSequenceData);
		    HadoopUtil.delete(new Configuration(), output);
		    //输入文件数据之间一定要以空格隔开，否则解析会出错
		    run(new Path(inputSequenceData), output, new EuclideanDistanceMeasure(), 0.3, 0.6);
		}
	}
	
	public static void run(Path input,Path output,DistanceMeasure dm,double t1,double t2) throws Exception{
		try {
			//将Text数据转换为适应聚类的vectorWritable数据
			//InputDriver.runJob(input, output,  "org.apache.mahout.math.RandomAccessSparseVector");
			
			//运行canopy算法
			CanopyDriver.run(input, output, dm , t1, t2, true, 0, true);
			
			//将聚类结果打印出来
		    ClusterDumper clusterdumper=new ClusterDumper(new Path(clusterOutputSequenceData+"clusters-0-final"),
				new Path(clusterOutputSequenceData+"clusteredPoints"));
		    
			clusterdumper.printClusters(null);
			
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		addInputOption();
		addOutputOption();
		/*
		 * 这边还可以添加别的参数的输出
		 */
		addOption(DefaultOptionCreator.distanceMeasureOption().create());
		addOption(DefaultOptionCreator.t1Option().create());
		addOption(DefaultOptionCreator.t2Option().create());
		addOption(DefaultOptionCreator.overwriteOption().create());
		
		Map<String,List<String>> maparg=parseArguments(args);
		if(maparg==null){
			System.out.print("解析参数错误");
			return -1;
		}
		
		Path input=getInputPath();
		Path output=getOutputPath();
		if(hasOption(DefaultOptionCreator.OVERWRITE_OPTION)){
			HadoopUtil.delete(new Configuration(), output);
		}		
		String measureClass=getOption(DefaultOptionCreator.DISTANCE_MEASURE_OPTION);
		double t1=Double.parseDouble(getOption(DefaultOptionCreator.T1_OPTION));
		double t2=Double.parseDouble(getOption(DefaultOptionCreator.T2_OPTION));
		//根据measureClass得到measure类
		DistanceMeasure measure = ClassUtils.instantiateAs(measureClass, DistanceMeasure.class);
		run(input, output, measure, t1, t2);
		return 0;
	}

}
