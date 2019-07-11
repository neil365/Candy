package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import candy.test.inf.InfTestCandy;
import cn.spr.framework.candy.server.CandyService;

/**
 * <p>Title: InfTestCandyImpl.java<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月27日
 * @version 1.0
 */

@CandyService(InfTestCandy.class)
public class InfTestCandyImpl implements InfTestCandy{

	@Override
	public void TestCandy() {
		System.out.println("TestCandy被调用");
	}

	@Override
	public List TestList(Map map) {
		// TODO Auto-generated method stub
		
		List list = new ArrayList();
		list.add("1");
		list.add("2");
		System.out.println("TestList被调用"+map);
		
		list.add(map);
		return list;
	}

}
