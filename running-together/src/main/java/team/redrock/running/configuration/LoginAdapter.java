package team.redrock.running.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import team.redrock.running.inteceptor.LoginInterceptor;

/**
 * @program: 
 * @description:
 * @author: mtb
 * @create: 2018-10-18 10:50
 **/
@Configuration
public class LoginAdapter extends WebMvcConfigurerAdapter {
	@Autowired
	LoginInterceptor loginInterceptor;
 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
 
		registry.addInterceptor(loginInterceptor)
//				.addPathPatterns("/**")
				.excludePathPatterns("/user/login")
				.excludePathPatterns("/mobilerun/head_img/**")
				.excludePathPatterns("/head_img/**");
		super.addInterceptors(registry);
	}
 
	/**
	 * 配置静态访问资源
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
 
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("classpath:/WEB-INF/classes/head_img/");
		registry.addResourceHandler("/head_img/");
 
		super.addResourceHandlers(registry);
 
	}
 
}