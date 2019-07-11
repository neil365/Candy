package cn.spr.framework.candy.registry;

import org.junit.Test;

import cn.spr.framework.candy.registry.zookeeper.ServiceDiscovery;
import cn.spr.framework.candy.registry.zookeeper.ServiceRegistry;

/**
 * Unit test for simple App.
 */
public class AppTest
{
 
	@Test
    public void  Test1()
    {
    	
    	ServiceRegistry sr = new ServiceRegistry("192.168.1.204");
    	sr.register("testService");
    
    	
    	
    	System.out.println("connect success");
       
    }
	
	@Test
	public void Test2() {
		ServiceDiscovery sd = new ServiceDiscovery("192.168.1.204");
		
		System.out.println("discovery success==>"+sd.discover());
		
	}

   

    /**
     * Rigourous Test :-)
     */
	@Test
    public void testApp()
    {
       
    }
}
