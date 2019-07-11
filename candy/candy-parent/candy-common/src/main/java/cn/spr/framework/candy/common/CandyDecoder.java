package cn.spr.framework.candy.common;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <p>Title: Decoder.java<／p>
 * <p>Description: 解码  传入 需要反序列化的类 {@link CandyRequest}<／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月19日
 * @version 1.0
 */
public class CandyDecoder extends ByteToMessageDecoder {

	
	 private Class<?> genericClass;
	 
	 
	/**
	 * 
	 * @param genericClass 反序列化的 Class
	 */
	public CandyDecoder(Class<?> genericClass) {
		
		this.genericClass = genericClass;
		
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		if (in.readableBytes() < 4) // 少于头信息
			return;

		in.markReaderIndex(); // 标记当前的readIndex的位置

		int dataLength = in.readInt();

		if (dataLength < 0) {
			ctx.close();
		}
		
		if(in.readableBytes() < dataLength) {
			in.resetReaderIndex(); //还原索引位置
			
		}
		
		byte[] data = new byte[dataLength];
		
		in.readBytes(data);
		
		//将data转换成object
		
		Object obj = SerializationUtil.deserialize(data, genericClass);
		
		out.add(obj);

	}
	
	
	

}
