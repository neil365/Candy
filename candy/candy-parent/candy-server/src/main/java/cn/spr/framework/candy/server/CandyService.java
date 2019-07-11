package cn.spr.framework.candy.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * <p>Title: CandyService.java<／p>
 * <p>Description: 定义注解<／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月18日
 * @version 1.0
 */

@Target({ ElementType.TYPE })//注解用在接口上
@Retention(RetentionPolicy.RUNTIME)//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Component
public @interface CandyService {
		
	Class<?> value();
}
