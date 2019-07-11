package cn.spr.framework.candy.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <p>Title: Encoder.java<／p>
 * <p>Description: rpc 编码 <／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月23日
 * @version 1.0
 */
public class CandyEncoder extends MessageToByteEncoder{

	
	private Class<?> genericClass;
	
	public CandyEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		
		if(genericClass.isInstance(msg)) {
			byte[] data =  SerializationUtil.serialize(msg);
			
			out.writeInt(data.length);
			
			out.writeBytes(data);
			
			
		}
		
	}

}
