package mahout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;

//读取canopy运行结果（序列化文件）
public class ReadSequenceFile {
	
	public final static String dfsDir="hdfs://192.168.95.131:9000/";
	
	public static void main(String [] args){
		Configuration conf=new Configuration();
		//输出聚类中心
		
		List<Cluster> clusters=readClusterWritable(new Path(dfsDir+"user/mahout/Canopyoutput/clusters-0-final/"),conf);
		for (Cluster cluster : clusters){
			System.out.println("第"+cluster.getId()+"簇的中心向量为");
			for(int i=0;i<cluster.getCenter().size();i++){
				
				System.out.print("第"+i+"个属性："+cluster.getCenter().get(i));
			}
			System.out.println("");
		}
		System.out.println("-------------------------------");
		readWeightedPropertyVectorWritable(new Path(dfsDir+"user/mahout/Canopyoutput/clusteredPoints/"),conf);
		//int i=0;
		
	}
	//读取所有point聚类结果文件
	public static void readWeightedPropertyVectorWritable(Path path,Configuration conf){
		
		//List<HashMap<Text, Text>> PointsClusterResult=new  ArrayList<HashMap<Text,Text>>();
		for(Writable value:new SequenceFileDirValueIterable<Writable>(path,PathType.LIST,
				PathFilters.partFilter(),conf)){
			Class<? extends Writable> valueClass=value.getClass();
			System.out.println(valueClass);
			if(valueClass.equals(WeightedPropertyVectorWritable.class)){
				System.out.print(((WeightedPropertyVectorWritable)value).getProperties().keySet());
				System.out.println(((WeightedPropertyVectorWritable)value).getProperties().values());
				System.out.println(((WeightedPropertyVectorWritable)value).getVector());
				System.out.println(((WeightedPropertyVectorWritable)value).getWeight());
			}
			//if(valueClass.equals(obj))
		}
		
		
	}
	
	
	
	//聚类结果中心文件转换
	public static List<Cluster> readClusterWritable(Path clusterpath,Configuration conf){
		List<Cluster> clusters=new  ArrayList<Cluster>();
		for(Writable value:new SequenceFileDirValueIterable<Writable>(clusterpath,PathType.LIST,
				PathFilters.partFilter(),conf)){
			//PathFilters.partFilter()表示过滤条件为，只接受前缀为”part-“的文件
			//PathType.LIST返回list
			Class<? extends Writable> valueClass=value.getClass();
			System.out.println("class类型为："+valueClass);
			if(valueClass.equals(ClusterWritable.class)){
				ClusterWritable clusterWritable=(ClusterWritable)value;
				value=clusterWritable.getValue();
				valueClass=value.getClass();
			}
			if(valueClass.equals(Kluster.class)){
				clusters.add((Kluster)value);
			}
			if(valueClass.equals(Canopy.class)){
				Canopy canopy=(Canopy)value;
				clusters.add(new Kluster(canopy.getCenter(),canopy.getId(),canopy.getMeasure()));
			}else{
				throw new IllegalStateException("传入的文件不是聚类中心结果文件");			}
		}		
		return clusters;
	}
	
}
