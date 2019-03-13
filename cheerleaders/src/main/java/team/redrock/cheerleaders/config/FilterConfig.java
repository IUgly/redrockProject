package team.redrock.cheerleaders.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.redrock.cheerleaders.filter.OpenIdFilter;

@Configuration
public class FilterConfig {
 
    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new OpenIdFilter());
        registration.addUrlPatterns("/*");
        registration.setName("OpenIdFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean FilterRedirectBean(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new OpenIdFilter());
        registration.addUrlPatterns("/*");
        registration.setName("RedirectFilter");
        registration.setOrder(2);
        return registration;
    }
 
}