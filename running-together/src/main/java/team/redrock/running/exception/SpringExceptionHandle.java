package team.redrock.running.exception;

import org.apache.ibatis.binding.BindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.enums.UnicomResponseEnums;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;
import java.net.ConnectException;
import java.sql.SQLException;

/**
 * @program: 测试
 * @description:全局的异常处理类
 * @author: mtb
 * @create: 2018-10-19 11:38
 **/
@RestControllerAdvice(annotations={RestController.class, Controller.class})
public class SpringExceptionHandle {
 
 
	private static final Logger logger = LoggerFactory.getLogger(SpringExceptionHandle.class);
	/**
	 * 请求参数类型错误异常的捕获
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value={BindException.class})
	@ResponseBody
	@ResponseStatus(value= HttpStatus.BAD_REQUEST)
	public ResponseBean<String> badRequest(BindException e){
		logger.error("occurs error when execute method ,message {}",e.getMessage());
		return new ResponseBean<>(UnicomResponseEnums.BAD_REQUEST);
	}
 
	/**
	 * 404错误异常的捕获
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value={NoHandlerFoundException.class})
	@ResponseBody
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public ResponseBean<String> badRequestNotFound(BindException e){
		logger.error("occurs error when execute method ,message {}",e.getMessage());
		return new ResponseBean<>(UnicomResponseEnums.NOT_FOUND);
	}
 
	/**
	 * mybatis未绑定异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindingException.class)
	@ResponseBody
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseBean<String> mybatis(Exception e){
		logger.error("occurs error when execute method ,message {}",e.getMessage());
		return new ResponseBean<>(UnicomResponseEnums.BOUND_STATEMENT_NOT_FOUNT);
	}
	/**
	 * 自定义异常的捕获
	 * 自定义抛出异常。统一的在这里捕获返回JSON格式的友好提示。
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value={UnicomRuntimeException.class})
	@ResponseBody
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public <T> ResponseBean<T> sendError(UnicomRuntimeException exception, HttpServletRequest request){
		String requestURI = request.getRequestURI();
		logger.error("occurs error when execute url ={} ,message {}",requestURI,exception.message);
		return new ResponseBean<>(exception.status,exception.message);
	}
	/**
	 * 数据库操作出现异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value={SQLException.class, DataAccessException.class})
	@ResponseBody
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseBean<String> systemError(Exception e){
		logger.error("occurs error when execute method ,message {}",e.getMessage());
		return new ResponseBean<>(UnicomResponseEnums.DATABASE_ERROR);
	}
	/**
	 * 网络连接失败！
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value={ConnectException.class})
	@ResponseBody
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseBean<String> connect(Exception e){
		logger.error("occurs error when execute method ,message {}",e.getMessage());
		return new ResponseBean<>(UnicomResponseEnums.CONNECTION_ERROR);
	}
 
	@ExceptionHandler(value={Exception.class})
	@ResponseBody
	@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
	public ResponseBean<String> notAllowed(Exception e){
		logger.error("occurs error when execute method ,message {}",e.getMessage());
		return new ResponseBean<>(UnicomResponseEnums.METHOD_NOT_ALLOWED);
	}
 
 
}