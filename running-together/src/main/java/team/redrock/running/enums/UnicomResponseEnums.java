package team.redrock.running.enums;

public enum UnicomResponseEnums {

	SYSTEM_ERROR(-001,"系统异常"),
	NO_INVITATION(23, "没有收到邀约"),
	NOT_POSITION(10,"没有排名数据"),
	NOT_STUDENT_OR_NOT_REGISTER(-1001,"没有该学号或未使用约跑"),
	NOT_INVITED_INFO(-1002, "没有邀约信息"),
	BAD_REQUEST(-002,"错误的请求参数"),
	NOT_FOUND(-00,"找不到请求路径！"),
	CONNECTION_ERROR(-002,"网络连接请求失败！"),
	METHOD_NOT_ALLOWED(-002,"不合法的请求方式"),
	DATABASE_ERROR(-002,"数据库异常"),
	BOUND_STATEMENT_NOT_FOUNT(-002,"找不到方法！"),
	UPLOAD_FAIL(-003, "上传失败"),
	NO_USER_EXIST(002,"该学号没有跑步数据"),
	INVALID_PASSWORD(-002,"帐号或密码错误"),
	NO_PERMISSION(-002,"非法请求！"),
	SUCCESS_OPTION(005,"操作成功！"),
	NOT_MATCH(007,"用户名和密码不匹配"),
	SUCCESS(200,"OK"),
	LOGOUT_SUCCESS(003,"已退出登录"),
	FILEUPLOAD_SUCCESS(022,"上传成功"),
	NOLOGIN(023,"未登陆"),
	ILLEGAL_ARGUMENT(024,"参数不合法"),
	ERROR_IDCODE(025,"验证码不正确");

	private int status;
	private String message;
	private UnicomResponseEnums(int status, String message) {

		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}