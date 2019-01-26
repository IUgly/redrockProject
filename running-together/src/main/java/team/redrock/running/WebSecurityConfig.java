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
import team.redrock.running.util.JwtUtils;
import team.redrock.running.util.MD5Util;
import team.redrock.running.vo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录配置
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {
    public final static String SESSION_KEY = "username";

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/user/login");
        addInterceptor.excludePathPatterns("/**");

    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            String token = request.getHeader("Authorization");
            String timestamp = request.getHeader("timestamp");
            String signature = request.getHeader("signature");
//            System.out.println(timestamp);
//            System.out.println(signature);
//
//            System.out.println(MD5Util.encrypt(token + "." + timestamp + "." + "runningtogether"));
            if (JwtUtils.decode(token, User.class) == null) {
                response.getWriter().write("token error");
                return false;
            } else {
                if ((Long.parseLong(timestamp) + 1000) < System.currentTimeMillis() / 1000) {
                    response.getWriter().write("duplicate commit");
                    return false;
                } else {
                    try {
                        if (MD5Util.md5(token + "." + timestamp + "." + "runningtogether").equals(signature)) {
                            return true;
                        } else {
                            response.getWriter().write("authorization failed");
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }
    }
}