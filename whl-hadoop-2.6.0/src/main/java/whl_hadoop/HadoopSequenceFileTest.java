package whl_hadoop;

import java.io.IOException;
import java.net.URI;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

public class HadoopSequenceFileTest {
	public static  FileSystem fs;
    public static final String Output_path="se.txt";
    public static Random random=new Random();
    private static final String[] DATA={
          "One,two,buckle my shoe",
          "Three,four,shut the door",
          "Five,six,pick up sticks",
          "Seven,eight,lay them straight",
          "Nine,ten,a big fat hen"
         };
    public static Configuration conf=new Configuration();
    public static void write(String pathStr) throws IOException{
        Path path=new Path(pathStr);
        FileSystem fs=FileSystem.get(URI.create(pathStr), conf);
        SequenceFile.Writer writer=SequenceFile.createWriter(fs, conf, path, Text.class, IntWritable.class);
        Text key=new Text();
        IntWritable value=new IntWritable();
        for(int i=0;i<DATA.length;i++){
            key.set(DATA[i]);
            value.set(random.nextInt(10));
            System.out.println(key);
            System.out.println(value);           
            System.out.println(writer.getLength());//获取该seqenceFile的长度。seqenceFile长度=header+[record]+syn_marker
            writer.append(key, value);//添加record到sequenceFile中
           
        }
        writer.close();
    }
    public static void read(String pathStr) throws IOException{
        FileSystem fs=FileSystem.get(URI.create(pathStr), conf);
        SequenceFile.Reader reader=new SequenceFile.Reader(fs, new Path(pathStr), conf);
        Text key=new Text();
        IntWritable value=new IntWritable();
        int recordNum=1;
        while(reader.next(key, value)){ //next函数逐条读取sequenceFile中的record,然后将KEY,VALUE反序列化赋值给key ，value
            System.out.println("record:"+recordNum++);
            System.out.println("key值为："+key+" value值为："+value);
        }
    }
   
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        write(Output_path);
        read(Output_path);
    }   

}
