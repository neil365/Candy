package cn.spr.framework.candy.registry.zookeeper;
/**
 * <p>Title: Constant.java<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月13日
 * @version 1.0
 */
public class Constant {
	
	
	public static final String ZK_REG_PATH="/candy"; //注册节点
	
	public static final String ZK_DATA_PATH=ZK_REG_PATH+"/data";  //数据节点
	
	public static  final int SESSION_TIME_OUT=2000;

}
