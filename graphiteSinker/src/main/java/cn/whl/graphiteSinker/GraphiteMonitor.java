package cn.whl.graphiteSinker;

import cn.whl.graphiteSinker.bean.CommitResult;
import cn.whl.graphiteSinker.bean.ReceiverResult;
import cn.whl.graphiteSinker.bean.SenderResult;

import kafka.common.TopicAndPartition;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by didi on 2017/6/15.
 */
public enum GraphiteMonitor {

    INSTANCE;

    private static Logger logger= Logger.getLogger(GraphiteMonitor.class);
    private static final int receiveQueueMaxSize = 1000;
    private static final int sendQueueMaxSize = 20000;
    private static final int updateQueueMaxSize=1000;

    public static boolean offsetUpdateBlockingQueueLock=false;

    private static final int dataConsumerThreadMaxSize=1;
    private static final int dataParserThreadMaxSize=10;
    private static final int dataSendThreadMaxSize=2;
    private static final int offsetUpdateThreadMaxSize=2;

    public static  BlockingQueue<CommitResult> offsetUpdateBlockingQueue = new  LinkedBlockingQueue<CommitResult>(receiveQueueMaxSize);
    public static final BlockingQueue<ReceiverResult> receiveBlockingQueue = new LinkedBlockingQueue<ReceiverResult>(receiveQueueMaxSize);
    public static final BlockingQueue<SenderResult> sendBlockingQueue = new LinkedBlockingQueue<SenderResult>(sendQueueMaxSize);
    public static final BlockingQueue<Map<TopicAndPartition,OffsetAndMetadata>> updateBlockingQueue = new LinkedBlockingQueue<Map<TopicAndPartition,OffsetAndMetadata>>(updateQueueMaxSize);
//
    private static  ExecutorService dataConsumerExecutorService= Executors.newFixedThreadPool(dataConsumerThreadMaxSize);
    private static  ExecutorService dataParserExecutorService=Executors.newFixedThreadPool(dataParserThreadMaxSize);
    private static  ExecutorService dataSendrExecutorService=Executors.newFixedThreadPool(dataSendThreadMaxSize);
    private static  ExecutorService offsetUpdateExecutorService=Executors.newFixedThreadPool(offsetUpdateThreadMaxSize);


    static {
        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {

                dataConsumerExecutorService.shutdown();
                dataParserExecutorService.shutdown();
                dataSendrExecutorService.shutdown();

                logger.info("destroy ExecutorServices ");

                //重新开启
                //logger.info("重新开启");
                // start();

            }
        });
    }

    public static  void start(){

        //start sinker
        GraphiteDataSinker graphiteDataSinker=new GraphiteDataSinker();
        dataConsumerExecutorService.submit(graphiteDataSinker);

        //start parse
        for (int i=0;i<dataParserThreadMaxSize;i++){
            GraphiteDataParser graphiteDataParser=new GraphiteDataParser();
            dataParserExecutorService.submit(graphiteDataParser);
        }

        //start send
        for (int i=0;i<dataSendThreadMaxSize;i++){
            GraphiteDataSender graphiteDataSender=new GraphiteDataSender();
            dataSendrExecutorService.submit(graphiteDataSender);
        }

    }



}
