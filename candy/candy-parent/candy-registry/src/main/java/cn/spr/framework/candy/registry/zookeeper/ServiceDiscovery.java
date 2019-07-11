package cn.spr.framework.candy.registry.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;

/**
 * <p>Title: ServiceDiscovery.java<／p>
 * <p>Description: 基于zookeepr 为客户端  发现服务,实现负载<／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年4月19日
 * @version 1.0
 */
//@Configuration
public class ServiceDiscovery {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);
	
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	private String address;
	
	private List<String> nodelist =  new ArrayList<String>();
	
	
	/**
	 * 构造
	 * @param address
	 */
	public ServiceDiscovery(String address) {
		
		this.address=address;
		
		ZooKeeper zk  = connectzk();
		if (zk!=null) {
			//监听
			monitor(zk);
		}
		
	}
	
	
	
	
	
	
	public  void monitor(ZooKeeper zook) {
		
		try {
			
			List<String> nodelist = zook.getChildren(Constant.ZK_REG_PATH, event ->{								
				
				if (event.getType() == Event.EventType.NodeChildrenChanged) {
					
					monitor(zook);
					
				}
				
			});
			
			List<String> datalist= new ArrayList<String>(); //节点信息
			
			for(String node : nodelist) {
				logger.debug("node===="+node);
				byte[] bytes= zook.getData(Constant.ZK_REG_PATH+"/"+node, false, null);
				
				datalist.add(new String (bytes));
			}
			
			logger.debug("datalist {}" , datalist);
			
			this.nodelist=datalist;
			
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	/**
	 * 
	 * @return 调用地址
	 */
	public String discover() {
		String data = "";
		int size = nodelist.size();

		if (size > 0) {

			if (size == 1) {
				
				data = nodelist.get(0);
			} else {
				data = nodelist.get(ThreadLocalRandom.current().nextInt(size)); //当前线程的 随机数

			}

		}

		return data;
	}
	
	
	
	/**
	 * 连接zk
	 * @return
	 */
	private ZooKeeper connectzk() {
		
		ZooKeeper zk=null;
		try {

			 zk = new ZooKeeper(address,Constant.SESSION_TIME_OUT, event-> {
				 
				 if(event.getState()== Event.KeeperState.SyncConnected) { 
					 
					 logger.debug("zk 连接成功");
					 
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
	
	
	
	
	

}
