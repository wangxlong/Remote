package cn.whl.graphiteSinker.bean;


//import org.apache.kafka.common.TopicPartition;
import kafka.common.TopicAndPartition;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;

/**
 * Created by didi on 2017/6/15.
 */
public class CommitResult {

    private String topic;
    private int partition;
    private long offset;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
