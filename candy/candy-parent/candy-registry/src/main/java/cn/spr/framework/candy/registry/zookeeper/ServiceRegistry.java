package cn.spr.framework.candy.registry.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ServiceRegistry.java<／p>
 * <p>Description: 服务注册
 * 	用于所有服务的地址，端口，  并提供服务发现
 * <／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年4月19日
 * @version 1.0
 */
public class ServiceRegistry {
	
	private static Logger log = LoggerFactory.getLogger(ServiceRegistry.class);
		
	private String zkAddress; //zk地址
		
	private CountDownLatch countDownLatch = new  CountDownLatch(1); //需要等待 zk 连接成功
	
	public ServiceRegistry(String zkAddress) {
		this.zkAddress = zkAddress;
	}
	
	
	
 




	/**
	 * 连接
	 * @return
	 */              
	public ZooKeeper connectService() {
		ZooKeeper zk=null;
		try {
//			 zk = new ZooKeeper(zkAddress,SESSION_TIME_OUT, new Watcher() {
//				 
//				public void process(WatchedEvent event) {
//					//如果成功 等待程序继续执行
//					if( event.getState() == Event.KeeperState.SyncConnected){
//						
//						log.debug("zk 连接成功");
//						
//						countDownLatch.countDown();
//					}
//					
//				}
//				
//			});
			
			
			 zk = new ZooKeeper(zkAddress,Constant.SESSION_TIME_OUT, event-> {
				 
				 if(event.getState()== Event.KeeperState.SyncConnected) { 
					 
					 log.debug("zk 连接成功");
					 
					 countDownLatch.countDown();
				 
				 }
			 }); 
			
			countDownLatch.await();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return zk;
	}
	
	
	
	
	/**
	 * 注册  并添加节点信息
	 * @param data
	 */
	public void register (String data) {
		
		if(data!=null && !"".equals(data)) {
			
			ZooKeeper zk =connectService();
			//建立节点
			if(zk!=null) {
				createNode(zk,data);
				
			}
			
		}
	}
	
	
	
	/**
	 * 创建节点
	 * @return
	 */
	private int createNode(ZooKeeper zk , String data ) {
		try {
			
		/**
		 * @param path
	     *                the path for the node
	     * @param data
	     *                the initial data for the node
	     * @param acl
	     *                the acl for the node    
	     *                (acl 访问控制列表)
	     * @param createMode
	     *                specifying whether the node to be created is ephemeral
	     *                and/or sequential
		 */								
		 
			byte[] bt = data.getBytes();
			
			//判断根节点存在
			if(zk.exists(Constant.ZK_REG_PATH, null) == null) {
				
				zk.create(Constant.ZK_REG_PATH, null, Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);				
				
			}
			
			
			String path  = zk.create(Constant.ZK_DATA_PATH, bt, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			
			log.info("建立节点:{}  --  {} " ,path,data);
		} catch (KeeperException | InterruptedException e) {
			// TODO Auto-generated catch block
				log.error("",e);
		}
		 
		 
		 return 0;
		 
		 
	}
	
	
		
	public static void main(String[] str) {
		
		 
		 
		ServiceRegistry sr = new ServiceRegistry("192.168.1.204");
		
    	sr.register("ctest");
    	
    	System.out.println("success");
    	
	}
	
	
	
}


