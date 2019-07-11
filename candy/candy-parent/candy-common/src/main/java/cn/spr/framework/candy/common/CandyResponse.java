package cn.spr.framework.candy.common;
/**
 * <p>Title: CandyResponse.java<／p>
 * <p>Description: 封装返回<／p>
 * <p>Copyright: Copyright (c) 2019<／p>
 * @author Neil Lee
 * @date 2019年6月19日
 * @version 1.0
 */
public class CandyResponse {
	
	
    private String requestId;
    private Throwable error;
    private Object result;

    public boolean isError() {
        return error != null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
