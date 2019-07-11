package cn.spr.framework.candy.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.spr.framework.candy.common.CandyRequest;
import cn.spr.framework.candy.common.CandyResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p>Title: CandyHandler.java<／p>
 * <p>Description: 类似过滤器 通过反射 获取具体的业务对象,执行并发回结果<／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月24日
 * @version 1.0
 */
public class CandyHandler extends SimpleChannelInboundHandler<CandyRequest>{
	
	private Logger logger = LoggerFactory.getLogger(CandyHandler.class);
	
	private Map<String, Object> handlerMap; //存放对象
	
	public CandyHandler(Map<String, Object> map) {
		this.handlerMap =map;
	}

	
	/**
	 * 接收到客户端消息时(老版本实现方法)
	 * 
	 */
//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, CandyRequest request) throws Exception {
//		
//		CandyResponse response = new CandyResponse();
//		
//		response.setRequestId(request.getRequestId());
//		
//		Object obj = this.getobject(request);
//		
//		response.setResult(obj);
//		
//		
//		//写入     （即CandyEncoder）进行下一步处理（即编码）后发送到channel中给客户端
//		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//		
//		
//	}
	
	
	
	
	/**
	 * 反射调用方法，并发回结果
	 * 
	 * @param request 封装的请求类
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private Object getobject(CandyRequest request) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		String classname = request.getClassName();
		
		Object bean = handlerMap.get(classname);
		
		String methodname = request.getMethodName();
		
		Class<?>[] parameterTypes = request.getParameterTypes(); //参数类型
		
		Object[] parameters = request.getParameters();
		
		Class<?> forname = Class.forName(classname);
		
		Method invokeMethod = forname.getMethod(methodname, parameterTypes);
		
		return invokeMethod.invoke(bean, parameters); 
	}


	
	/**
	 * 接收到客户端消息
	 */
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, CandyRequest request) throws Exception {
		
		logger.info("接收到客户端消息");

		CandyResponse response = new CandyResponse();
		
		response.setRequestId(request.getRequestId());
		
		Object obj = this.getobject(request);
		
		response.setResult(obj);
		
		
		//写入     （即CandyEncoder）进行下一步处理（即编码）后发送到channel中给客户端
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		
		
	}
	
	

}
