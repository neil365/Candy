package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Title: CandyTestController.java<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月27日
 * @version 1.0
 */




@Controller
public class CandyTestController {
	Logger logger = LoggerFactory.getLogger(CandyTestController.class);
	
	@RequestMapping("test/candy")
	public void test() {
		logger.debug("test======================");
	}
	
	
	

}
