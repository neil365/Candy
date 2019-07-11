package cn.spr.framework.candy.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.spr.framework.candy.common.CandyDecoder;
import cn.spr.framework.candy.common.CandyEncoder;
import cn.spr.framework.candy.common.CandyRequest;
import cn.spr.framework.candy.common.CandyResponse;
import cn.spr.framework.candy.registry.zookeeper.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <p>Title: CandyServer.java<／p>
 * <p>Description: 框架服务器端<／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月18日
 * @version 1.0
 */

@Component
public class CandyServer implements ApplicationContextAware,InitializingBean {

	
	private Logger logger = LoggerFactory.getLogger(CandyServer.class);
	
	private String serverAdderss; 
	
	private ServiceRegistry registry;
	
	//用于存储业务接口和实现类的实例对象(由spring所构造)
	private Map<String, Object> handlerMap = new HashMap<String, Object>();
	
	
	
	
	public CandyServer(String serverAdress) {
		logger.debug("init====");
		this.serverAdderss = serverAdress;
	}
	
	
	public CandyServer(String serverAdress, ServiceRegistry registry) {
		this.serverAdderss = serverAdress;
		this.registry = registry;
	}
	
	/**
	 * 获取业务类BEAN的集合 并发放 map中
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		
		logger.debug("setapplicationcontext==========");
		Map<String, Object> map = arg0.getBeansWithAnnotation(CandyService.class);
		

		if(map != null ) {
			
			for (Object serviceBean : map.values()) {
				String interfaceName = serviceBean.getClass().getAnnotation(CandyService.class).value().getName();
				
				logger.info("interface== {} , servicebean=={}" ,interfaceName,serviceBean);
				
				handlerMap.put(interfaceName, serviceBean);
			}
		}
		
		
	}
	
	
 
	/**
	 * spring 初始化容器时调用 并启动NETTY
	 *  接收数据并反序列化
	 *  调用业务的实现方法 
	 *  再返回结果 
	 *  
	 * Netty框架的主要线程就是I/O线程，线程模型设计的好坏，决定了系统的吞吐量、并发性和安全性等架构质量属性。
	 * Netty的线程模型被精心地设计，既提升了框架的并发性能，又能在很大程度避免锁，局部实现了无锁化设计。
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		new Thread(()-> {
				
			// 创建了两个NioEventLoopGroup，它们实际是两个独立的Reactor线程池。一个用于接收客户端的TCP连接，另一个用于处理I/O相关的读写操作，或者执行系统Task、定时任务Task等。
			logger.debug("=====afterPropertiesSet===========");
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {	

				ServerBootstrap bootstrap = new ServerBootstrap(); 
				
				bootstrap.group(bossGroup, workerGroup)// 注册两个eventloopgrup  白话:我们需要两种类型的人干活，一个是老板，一个是工人，老板负责从外面接活，接到的活分配给工人干，放到这里，bossGroup的作用就是不断地accept到新的连接，将新的连接丢给workerGroup来处理
				
				.channel(NioServerSocketChannel.class) //服务端启动的是nio相关的channel
				.childHandler(new ChannelInitializer<SocketChannel>() { //表示一条新的连接进来之后，该怎么处理，也就是上面所说的，老板如何给工人配活
		
					@Override
					public void initChannel(SocketChannel channel) //这个方法在Channel被注册到EventLoop的时候会被调用
							throws Exception {
						channel.pipeline()
								.addLast(new CandyDecoder(CandyRequest.class))// 注册解码 IN-1
								.addLast(new CandyEncoder(CandyResponse.class))// 注册编码 OUT
								.addLast(new CandyHandler(handlerMap));//注册RpcHandler IN-2
					}
					
				}).option(ChannelOption.SO_BACKLOG, 128) //socket标准参数
				.childOption(ChannelOption.SO_KEEPALIVE, true);
				
				String[] str = serverAdderss.split(":");
				String host = str[0];
				String port = str[1];
				
				ChannelFuture future = bootstrap.bind(host, Integer.parseInt(port)).sync(); //启动
				
				logger.info("启动："+host +":"+port);
				if(registry!=null) {
					registry.register(serverAdderss);
				}
				
				future.channel().closeFuture().sync(); //这里会阻塞，进入无限循环， 需要用线程池执行
				logger.info("closeFuture====");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
			
			 }).start();
		

		
	}

}
