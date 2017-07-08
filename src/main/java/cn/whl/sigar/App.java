package cn.whl.sigar;

import java.util.Map;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONArray;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SigarException, InterruptedException
    {
    	final String topic="machineCollecting";
    	final boolean isAsyn=false;
    	Sigar sigar = SigarUtils.sigar;
    	KafkaProducer<String, String> productor=KafkaProductorCreate.getKafkaProductor();
    	try{
        	while(true){
        		
            	JSONArray js=SigarUtils.infoToJsonArray(sigar);
            	if(productor!=null){
            		if(isAsyn){
            			productor.send(new ProducerRecord<String, String>(topic,js.toString()));
            		}else{
                		productor.send(new ProducerRecord<String, String>(topic, js.toString()),
                        		new Callback(){
            						public void onCompletion(RecordMetadata rM, Exception e) {
            							// TODO Auto-generated method stub
            							e.printStackTrace();
            							//System.out.print("已发送："+rM.offset()+","+rM.partition()+","+rM.topic()+",");
            						}
                        });
            		}
            	}
            	//System.out.println(js.toString());
            	Thread.currentThread().sleep(10000);
        	}
    	}catch(SigarException e){
    		
    	}finally{
    		sigar.close();
    	}  	
    }

}
