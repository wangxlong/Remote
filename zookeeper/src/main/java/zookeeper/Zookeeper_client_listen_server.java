package zookeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;


public class Zookeeper_client_listen_server {
	
	private String groupNode = "sgroup";  
    private ZooKeeper zk;  
    private Stat stat = new Stat();  
    private volatile List<String> serverList;  

	public void zookeeperconnect() throws KeeperException, InterruptedException{
		
		try {
			 zk=new ZooKeeper("localhost:2181",5000,new Watcher(){				
				public void process(WatchedEvent event) {
					// TODO Auto-generated method stub
					if(event.getType()==EventType.NodeChildrenChanged&&event.getPath().equals(("/"+groupNode))){
						try {
							updateserverList();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (KeeperException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			});
			updateserverList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateserverList() throws KeeperException, InterruptedException, UnsupportedEncodingException{
		 List<String> newServerList = new ArrayList<String>();  
		  
	        // 获取并监听groupNode的子节点变化  
	        // watch参数为true, 表示监听子节点变化事件.   
	        // 每次都需要重新注册监听, 因为一次注册, 只能监听一次事件, 如果还想继续保持监听, 必须重新注册  
	        List<String> subList = zk.getChildren("/sgroup", true);  
	        for (String subNode : subList) {  
	            // 获取每个子节点下关联的server地址  ,server名字
	        	//stat为写入参数，即先new 一个stat对象，传入该方法，则该方法会把data的状态写在stat对象
	            byte[] data = zk.getData("/" + groupNode + "/" + subNode, false, stat);  
	            newServerList.add(new String(data, "utf-8"));  //server名字列表
	        }  
	  
	        // 替换server列表  
	        serverList = newServerList;  
	  
	        System.out.println("server list updated: " + serverList);  
	}
	
	 /** 
     * client的工作逻辑写在这个方法中 
     * 此处不做任何处理, 只让client sleep 
     */  
    public void handle() throws InterruptedException {  
        Thread.sleep(Long.MAX_VALUE);  
    }  
  
    public static void main(String[] args) throws Exception {  
    	Zookeeper_client_listen_server ac = new Zookeeper_client_listen_server();  
        ac.zookeeperconnect();
        ac.handle();  
    } 
}

