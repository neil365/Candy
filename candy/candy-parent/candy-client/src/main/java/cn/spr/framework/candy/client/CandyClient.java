package cn.spr.framework.candy.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.spr.framework.candy.common.CandyDecoder;
import cn.spr.framework.candy.common.CandyEncoder;
import cn.spr.framework.candy.common.CandyRequest;
import cn.spr.framework.candy.common.CandyResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * <p>Title: CandyClient.java<／p>
 * <p>Description: 客户端用于发送远程请求<／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月28日
 * @version 1.0
 */
public class CandyClient extends SimpleChannelInboundHandler<CandyResponse>{
	
	
	Logger logger= LoggerFactory.getLogger(CandyClient.class);
	private final Object obj = new Object();
	
	private String host;
	private int port;

	private CandyResponse response;
	
	
	public CandyClient(String host, int port) {
		this.host = host;
		this.port= port;
	}
	
	/**
	 * 发消息
	 * @param request
	 * @return
	 * @throws InterruptedException 
	 */
	public CandyResponse send (CandyRequest request) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>(){
	
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					
					ch.pipeline().addLast(new CandyEncoder(CandyRequest.class))
					.addLast(new CandyDecoder(CandyResponse.class))
					.addLast(CandyClient.this);
					
				}
				
				
			}).option(ChannelOption.SO_KEEPALIVE, true);
						
				ChannelFuture future= bootstrap.connect(host, port).sync(); //连接服务器
				
				future.channel().writeAndFlush(request).sync();//将request对象写入outbundle 处理后发出
				
				synchronized (obj) {
					obj.wait();  // 这里阻塞， 等服务器返回
				}
				
				if(response != null ) {
					future.channel().closeFuture().sync(); //服务器断开时 执行
				}
				
				return response;
				
		} 
		
		finally {
			group.shutdownGracefully(); //退出
		}
		
		 
		 
	}



	/**
	 * 接收消息
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, CandyResponse msg) throws Exception {
		 this.response = msg;
		 
		 synchronized (obj) {
				obj.notifyAll(); //线程唤醒
			}
		
	}

 
	
	
	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("client caught exception", cause);
		ctx.close();
	}



	
	public static void main(String str[]) {
		CandyClient cc = new CandyClient("localhost",8889);
		
		CandyRequest cr = new CandyRequest();
		cr.setRequestId("11");
		cr.setClassName("candy.test.inf.InfTestCandy");
		cr.setMethodName("TestCandy");
		
		try {
			cc.send(cr);
		} catch (InterruptedException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}




}
