package zookeeper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

//利用zookeeper实现分布式锁
public class Zookeeper_locks {
	private static final int session_time=5000;
	private String zookeeperaddress="localhost:2181";
	private String node="Lock";
	private String subnode="servernunber";
	private ZooKeeper zk;
	private CountDownLatch countdown;
	private String thispath;
	private String listennode;
	public void zookeeperconn() throws Exception{
	    countdown=new CountDownLatch(1);
		try {
			zk=new ZooKeeper(zookeeperaddress,session_time,new Watcher(){

				public void process(WatchedEvent event) {//连接成功后回调process方法
					// TODO Auto-generated method stub
					if(event.getState()==KeeperState.SyncConnected){
						countdown.countDown();//控制同步连接
					}
				}	
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			countdown.await();			//控制同步，zookeeper会话建立和对象创建过程是异步的，
			                            //所以等待连接成功后继续执行
			thispath=zk.create("/"+node+"/"+subnode,null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		    //判断自己是否获得了锁，如果获得了就执行相应的操作，否则监听获得
			// wait一小会, 让结果更清晰一些  
	        Thread.sleep(10);  
	  
	        // 注意, 没有必要监听"/locks"的子节点的变化情况  
	        List<String> childrenNodes = zk.getChildren("/" +node, false);  
	  
	        // 列表中只有一个子节点, 那肯定就是thisPath, 说明client获得锁  
	        if (childrenNodes.size() == 1) {  
	            doSomething();  
	        } else {  
	            String thisNode = thispath.substring(("/" +node + "/").length());  
	            // 排序  
	            Collections.sort(childrenNodes);  
	            int index = childrenNodes.indexOf(thisNode);  
	            if (index == -1) {  
	                // never happened  
	            } else if (index == 0) {  
	                // inddx == 0, 说明thisNode在列表中最小, 当前client获得锁  
	                doSomething();  
	            } else {  
	                // 获得排名比thisPath前1位的节点  
	                this.listennode = "/" + node + "/" + childrenNodes.get(index - 1);  
	                // 在waitPath上注册监听器, 当waitPath被删除时, zookeeper会回调监听器的process方法  
	                zk.getData(listennode, true, new Stat());  	 			
			
		}
	        }
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	        private void doSomething() throws Exception {  
	            try {  
	                System.out.println("gain lock: " + thispath);  
	                Thread.sleep(2000);  
	                // do something  
	            } finally {  
	                System.out.println("finished: " + thispath);  
	                // 将thisPath删除, 监听thisPath的client将获得通知  
	                // 相当于释放锁  
	                zk.delete(this.thispath, -1);  
	            }  
	        }  
	      
	        public static void main(String[] args) throws Exception {  
	            for (int i = 0; i < 10; i++) {  
	                new Thread() {  
	                    public void run() {  
	                        try {  
	                        	Zookeeper_locks dl = new Zookeeper_locks();  
	                            dl.zookeeperconn();  
	                        } catch (Exception e) {  
	                            e.printStackTrace();  
	                        }  
	                    }  
	                }.start();  
	            }  
	      
	            Thread.sleep(Long.MAX_VALUE);  
	        }  
}
