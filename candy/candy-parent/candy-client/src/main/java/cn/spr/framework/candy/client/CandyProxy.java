package cn.spr.framework.candy.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import cn.spr.framework.candy.common.CandyRequest;
import cn.spr.framework.candy.common.CandyResponse;
import cn.spr.framework.candy.registry.zookeeper.ServiceDiscovery;

/**
 * <p>Title: CandyProxy.java<／p>
 * <p>Description:  客户端调用 代理<／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月28日
 * @version 1.0
 */
public class CandyProxy {
	
	private String serverAddress;
	
	private ServiceDiscovery discovery;
	
	
	public CandyProxy(String address, ServiceDiscovery discovery ) {
		
		
		this.serverAddress = address;
		this.discovery = discovery;
	}
	
	
	
	
	/**
	 * 动态代理实现
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public<T> T  create (Class<T> interfaces) {
		
		 return  (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[] {interfaces},
				 
				 (proxy,method,args) ->  {
				
					CandyRequest request =  new CandyRequest();
	//				构造request请求所需要信息
					request.setRequestId(UUID.randomUUID().toString());
					
					request.setClassName(method.getDeclaringClass().getName());
					
					request.setMethodName(method.getName());
					
					request.setParameterTypes(method.getParameterTypes());
					
					request.setParameters(args);
					
					if(discovery!=null) {
						serverAddress = discovery.discover(); //服务器地址
					}
					
					
	                String[] array = serverAddress.split(":");
	                String host = array[0];
	                int port = Integer.parseInt(array[1]);
	                
	                //链接服务端
	                CandyClient client = new CandyClient(host,port);
	                
	                CandyResponse response =  client.send(request);
	               
	                
					return response.getResult();
				}
			
		);
		
		 
		
	}
	
	
	
	
	
	
	
}
