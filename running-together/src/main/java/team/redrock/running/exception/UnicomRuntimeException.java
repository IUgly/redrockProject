package team.redrock.running.exception;

import team.redrock.running.enums.UnicomResponseEnums;

public class UnicomRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	protected int status;
 
	protected String message;
 
	protected String messageInfo;//打印出的日志信息
 
	public UnicomRuntimeException(UnicomResponseEnums enums, String message) {
		super();
		this.status = enums.getStatus();
		this.message = enums.getMessage();
		this.messageInfo = message;
	}
 
	public UnicomRuntimeException(UnicomResponseEnums enums) {
		super();
		this.status = enums.getStatus();
		this.message = enums.getMessage();
	}
 


	public UnicomRuntimeException() {
		super();
	}
 
	public UnicomRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
 
	public UnicomRuntimeException(String message) {
		super(message);
	}
 
}