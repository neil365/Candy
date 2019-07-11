package cn.spr.framework.candy.common;

/**
 * <p>
 * Title: CandyRequest.java<／p>
 * <p>
 * Description: 封装 rpc 请求<／p>
 * <p>
 * Copyright: Copyright (c) 2019<／p>
 * 
 * @author Neil Lee
 * @date 2019年6月19日
 * @version 1.0
 */
public class CandyRequest {

	private String requestId;
	private String className;
	private String methodName;
	private Class<?>[] parameterTypes;
	private Object[] parameters;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

}
