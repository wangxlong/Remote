package cn.whl.graphiteSinker;

import cn.whl.graphiteSinker.bean.CommitResult;
import cn.whl.graphiteSinker.bean.ReceiverResult;
import cn.whl.graphiteSinker.conf.Constants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by didi on 2017/6/15.
 */
public class GraphiteDataSinker implements Runnable{

    private static Logger logger= Logger.getLogger(GraphiteDataSinker.class);

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    private KafkaConsumer<String, String> kafkaConsumer  = null;

    private List<String> consumeTopic=null;

    private final static  int  UPDATE_OFFSET_TRY_TIMES=5;

//    static {
//        Runtime.getRuntime().addShutdownHook(new Thread(){
//            @Override
//            public void run() {
//               // logger.info("close graphitedatasinker");
//
//            }
//        });
//    }

    /**
     *初始化
     * @param topicNames
     */
    private boolean init(String ... topicNames){

        if(consumeTopic==null){
            consumeTopic=new ArrayList<String>();
        }
        for(String topicName:topicNames){
            consumeTopic.add(topicName);
        }

        Properties props = new Properties();
        props.put("bootstrap.servers", Constants.BOOTSTRAP_SERVERS);
        props.put("group.id", Constants.GROUP_ID);
        //自动提交
        props.put("enable.auto.commit", "false");
        //props.put("auto.offset.reset","earliest");

        //key和value的序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        if (kafkaConsumer==null){
            kafkaConsumer = new KafkaConsumer<String, String>(props);

        }
        kafkaConsumer.subscribe(consumeTopic);

        return true;

    }

    /**
     * 消费
     */
    public void consume(){

        try {
            while (!Thread.currentThread().isInterrupted()) {


                BlockingQueue<CommitResult> toOffsetUpdateTmpBlockingQueue=null;
                synchronized (GraphiteDataSinker.class) {
                    toOffsetUpdateTmpBlockingQueue= new LinkedBlockingQueue<CommitResult>(1000);
                    GraphiteMonitor.offsetUpdateBlockingQueueLock=true;
                    toOffsetUpdateTmpBlockingQueue= GraphiteMonitor.offsetUpdateBlockingQueue;
                    //replace the original map to prevent data change when iterator data
                    GraphiteMonitor.offsetUpdateBlockingQueue = new LinkedBlockingQueue<CommitResult>(1000);
                    GraphiteMonitor.offsetUpdateBlockingQueueLock = false;
                }

                int  updateTime;

                while(toOffsetUpdateTmpBlockingQueue.peek()!=null){
                    CommitResult commitMap=toOffsetUpdateTmpBlockingQueue.take();
                    updateTime=5;//提交失败重试5次

                    while(updateTime!=0){
                        try {
                            kafkaConsumer.commitSync(Collections.singletonMap(new TopicPartition(commitMap.getTopic(), commitMap.getPartition()), new OffsetAndMetadata(commitMap.getOffset() + 1)));
                            logger.info("-------------update offset-----" + "topic=" + commitMap.getTopic() + " partition=" + commitMap.getPartition() + " offset=" + commitMap.getOffset());
                            updateTime=0;
                        }catch(Exception e){
                            updateTime--;
                            logger.error("失败update重试次数倒数："+updateTime+" "+e.getMessage());
                        }
                    }

                }

                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records){

//                    logger.info("partition=%d,topic=%s,offset = %d, key = %s, value = %s%n", record.partition(),record.topic(),record.offset(), record.key(), record.value());
                    ReceiverResult receiverResult=new ReceiverResult();
                    receiverResult.setMessage(record.value());
                    receiverResult.setTopic(record.topic());
                    receiverResult.setPartition(record.partition());
                    receiverResult.setOffset(record.offset());

//                    logger.info("receiverResult="+receiverResult.toString());

                    GraphiteMonitor.receiveBlockingQueue.put(receiverResult);

//                    Thread.sleep(15000); //睡面1分钟

                }
                //consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }finally {
            kafkaConsumer.close();
        }

    }

    /**
     * 运行
     */
    public void run() {
        if(init(Constants.TOPIC_NAME)){
            consume();
        }

    }

}
