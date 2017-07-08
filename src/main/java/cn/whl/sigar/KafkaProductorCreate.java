package cn.whl.sigar;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaProductorCreate {

	
	public static KafkaProducer<String, String> getKafkaProductor(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "115.159.34.36:9092");//broker 集群地址
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProductor");//自定义客户端id
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");//key 序列号方式
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");//value 序列号方式
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        return producer;
	}
	
	
}
