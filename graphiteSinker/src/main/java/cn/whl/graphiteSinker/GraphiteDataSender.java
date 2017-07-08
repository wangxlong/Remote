package cn.whl.graphiteSinker;

import cn.whl.graphiteSinker.bean.CommitResult;
import cn.whl.graphiteSinker.bean.SenderResult;
import cn.whl.graphiteSinker.conf.Constants;
import kafka.common.TopicAndPartition;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

/**
 * Created by didi on 2017/6/15.
 */
public class GraphiteDataSender implements Runnable{


    private static Logger logger=Logger.getLogger(GraphiteDataSender.class);
    private Socket socket = null;
    private static Object kafkaConsumerLock=new Object();

    public Boolean init(){

        boolean flag=true;
        while(flag){
            try {
                //创建一个流套接字并将其连接到指定主机上的指定端口号
                if(socket==null){
                    socket = new Socket(Constants.GRAPHITE_HOST, Constants.GRAPHITE_PORT);
                    flag=false;
                }
            }catch (IOException e){
                flag=true;
                logger.error("client can not connect to "+Constants.GRAPHITE_HOST+":"+Constants.GRAPHITE_PORT+"," + e.getMessage());
            }finally {

            }

        }
        return true;
    }

    /**
     *发数据给graphite客户端
     * eg:String countGraphiteMessage=countDataPath+" "+count+" "+timestamp+"\n";
     */
    public void sendMessageToGraphite() {

        DataOutputStream out = null;

        try {
            //向服务器端发送数据
            out= new DataOutputStream(socket.getOutputStream());
            while(!Thread.currentThread().isInterrupted()){
                SenderResult senderResult=GraphiteMonitor.sendBlockingQueue.take();
                List<String> messageList=senderResult.getMessageList();
                String topic = senderResult.getTopic();
                int partition = senderResult.getPartition();
                long offset = senderResult.getOffset();

                Iterator<String> messageListIt=messageList.iterator();

                while(messageListIt.hasNext()){

                    //logger.info("send message="+messageListIt.next()+" topic="+topic+" partition="+partition+" offset="+String.valueOf(offset));
                    out.write(messageListIt.next().getBytes());
                }
                out.flush();

                //锁住
                while(GraphiteMonitor.offsetUpdateBlockingQueueLock){

                }

                CommitResult commitResult=new CommitResult();
                commitResult.setOffset(offset);
                commitResult.setPartition(partition);
                commitResult.setTopic(topic);

                GraphiteMonitor.offsetUpdateBlockingQueue.put(commitResult);

//                Thread.sleep(5000); //睡面1分钟
                //GraphiteMonitor.updateBlockingQueue.put(Collections.singletonMap(topicAndPartition,offsetAndMetadata));

            }
        } catch (Exception e) {

            logger.error(e.getMessage());

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    out.close();

                } catch (IOException e) {
                    logger.error("client close error:" + e.getMessage());
                }
            }
        }
    }


    public void run() {

        if(init()){
            sendMessageToGraphite();
        }
    }
}
