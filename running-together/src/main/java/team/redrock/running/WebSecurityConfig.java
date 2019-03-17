package team.redrock.running;

/**
 * Created by huangds on 2017/10/24.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import team.redrock.running.util.MD5Util;
import team.redrock.running.util.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录配置
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

//        addInterceptor.excludePathPatterns("/error");
//        addInterceptor.excludePathPatterns("/user/login");
        addInterceptor.addPathPatterns("/user/distance/update");
        addInterceptor.addPathPatterns("/invite/update_data");
        addInterceptor.addPathPatterns("/mobilerun/user/distance/update");
        addInterceptor.addPathPatterns("/mobilerun/invite/update_data");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            String strToken = request.getHeader("token");
            String timestamp = request.getHeader("timestamp");
            String signature = request.getHeader("signature");

            System.out.println("token: "+strToken);
            System.out.println("time: "+timestamp);
            System.out.println("signature: "+signature);

            boolean flag = false;
            try {
                Token token = Token.CreateFrom(strToken);
                if (!token.IsExpired()&&token!=null){
                    flag =true;
                }
            }catch (Exception e){
                flag =false;
                e.printStackTrace();
            }
            if (!flag) {
                response.getWriter().write("token error");
                return false;
            } else {
                if ((Long.parseLong(timestamp) + 2) < System.currentTimeMillis()/1000 ) {
                    response.getWriter().write("duplicate commit");
                    return false;
                } else {
                    try {
                        if (MD5Util.md5(strToken + "." + timestamp + "." + "runningtogether").equals(signature)) {
                            return true;
                        } else {
                            response.getWriter().write("authorization failed");
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
    }
}