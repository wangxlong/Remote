package hdfs;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import Util.Utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileInputAndOutput{
	
	
	public static void main(String []args) throws IOException{
		readFromHdfs("user/input/count1.txt");
	}
	
	//java读hdfs中不是序列化的文件,读取序列化的文件使用map reduce
	public static void readFromHdfs(String filename) throws IOException{
		Configuration conf=new Configuration();
		FileSystem fs=FileSystem.get(URI.create(Utils.dfsDir+filename), conf);
		if(fs.isFile(new Path(Utils.dfsDir+filename))){
			System.out.println("file exit");
			FSDataInputStream dataintput=fs.open(new Path(Utils.dfsDir+filename));
			OutputStream out = new FileOutputStream("qq-hdfs.txt");
			byte[] ioBuffer = new byte[1024];
			int readLen = dataintput.read(ioBuffer);
			while(-1 != readLen){
			  out.write(ioBuffer, 0, readLen); 
			  readLen = dataintput.read(ioBuffer);
			 }
			 out.close();
			 dataintput.close();
			 fs.close();
		}else{
			System.out.print("不是文件");
		}
	}
}