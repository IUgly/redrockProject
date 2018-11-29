package team.redrock.running.bean;

import lombok.Data;
import team.redrock.running.enums.UnicomResponseEnums;

@Data
public class ResponseBean <T>{
//	private boolean success;
	private T data;
	private int status;
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
	public ResponseBean(T data, int errCode, String errMsg) {
		super();
		this.data = data;
		this.status = errCode;
		this.message = errMsg;
	}
	public ResponseBean(int errCode, String errMsg) {
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

}