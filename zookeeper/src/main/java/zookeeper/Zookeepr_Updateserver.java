package zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;


//分布式系统服务端代码，每增加一个服务端，则会在zookeeper的sgroup节点下建立一个临时序列子节点。当这个服务端蹦掉了，
//则该服务端在zookeeper下的节点就会消失，因为所建的节点为临时节点。如果客户端对zookeeper的sgroup节点进行监听的话
//则当该节点变化时，客户端就可知道服务端的个数的变化。

public class Zookeepr_Updateserver {

	    private String groupNode = "sgroup";  
	    private String subNode = "sub";  
	  
	    /** 
	     * 连接zookeeper 
	     * @param address server的地址 
	     */  
	    public void connectZookeeper(String address) throws Exception {  
	        ZooKeeper zk = new ZooKeeper("localhost:2181", 5000, new serverwatcher()); 
	        // 在"/sgroup"下创建子节点  
	        // 子节点的类型设置为EPHEMERAL_SEQUENTIAL, 表明这是一个临时节点, 且在子节点的名称后面加上一串数字后缀  
	        // 将server的地址数据关联到新创建的子节点上  
	        String createdPath = zk.create("/" + groupNode + "/" + subNode, address.getBytes("utf-8"),   
	            Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);  
	        //Ids.OPEN_ACL_UNSAFE表示不设定权限
	        //CreateMode.EPHEMERAL_SEQUENTIAL表示创建序列性的临时节点
	        System.out.println("create: " + createdPath);  
	    }  
	      
	    /** 
	     * server的工作逻辑写在这个方法中 
	     * 此处不做任何处理, 只让server sleep 
	     */  
	    public void handle() throws InterruptedException {  
	        Thread.sleep(Long.MAX_VALUE);  
	    }  
	      
	    public static void main(String[] args) throws Exception {  
	        // 在参数中指定server的地址  
	        if (args.length == 0) {  
	            System.err.println("The first argument must be server address");  
	            System.exit(1);  
	        }  
	          
	        Zookeepr_Updateserver as = new Zookeepr_Updateserver();  
	        as.connectZookeeper(args[0]);  
	        as.handle();  
	    }  
	}  

  class serverwatcher implements Watcher{

	public void process(WatchedEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("do noting！");
	}
  }
