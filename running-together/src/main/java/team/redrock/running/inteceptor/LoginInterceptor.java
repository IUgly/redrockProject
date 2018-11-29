package team.redrock.running.inteceptor;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.controller.UserControl;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.util.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
@EnableAsync
public class LoginInterceptor implements HandlerInterceptor {
 
	private static Logger logger=Logger.getLogger(LoginInterceptor.class);
 
	@Autowired
	private UserControl userControl;
	//    在请求处理之前调用,只有返回true才会执行请求
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		String strToken = httpServletRequest.getHeader("token");
		strToken = Base64.encode(strToken.getBytes());

		Token token = Token.CreateFrom(strToken);
		//登录过得用户直接通过
		if(token!=null&&!token.IsExpired()){
			return true;
		}
 
		//未登录用户拦截，返回json数据
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json; charset=utf-8");
		PrintWriter out = httpServletResponse.getWriter() ;
		try{
 
			ResponseBean<Object> responseBean=new ResponseBean<>(UnicomResponseEnums.NOLOGIN);
			out = httpServletResponse.getWriter();
			out.append(JSON.toJSON(responseBean).toString());
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			httpServletResponse.sendError(500);
			return false;
		}
 
	}
 
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
 
	}
 
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
 
	}
}