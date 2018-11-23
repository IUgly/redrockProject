package team.redrock.running.bean;

import team.redrock.running.enums.UnicomResponseEnums;

public class ResponseBean <T>{
//	private boolean success;
	private T data;
	private String status;
	private String message;
 
	public ResponseBean(){}
 
	public ResponseBean(T data) {
		super();
		this.data = data;
	}

	@Override
	public String toString() {
		return "{" +
				"data=" + data +
				", status='" + status + '\'' +
				", message='" + message + '\'' +
				'}';
	}

	public ResponseBean(T data, String errCode, String errMsg) {
		super();
//		this.success = success;
		this.data = data;
		this.status = errCode;
		this.status = errMsg;
	}
 
	public ResponseBean(String errCode, String errMsg) {
		this.status= errCode;
		this.message = errMsg;
	}
	public ResponseBean(UnicomResponseEnums enums){
		this.status=enums.getStatus();
		this.message=enums.getMessage();
	}
	public ResponseBean(T data,UnicomResponseEnums enums){
		this.data=data;
		this.status=enums.getStatus();
		this.message=enums.getMessage();
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}