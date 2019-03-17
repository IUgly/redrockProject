package team.redrock.cheerleaders.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.redrock.cheerleaders.filter.OpenIdFilter;
import team.redrock.cheerleaders.filter.RedirectFilter;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {
    @Bean
    public Filter OpenIdFilter(){
        return new OpenIdFilter();
    }

    @Bean
    public Filter RedirectFilter(){
        return new RedirectFilter();
    }
 
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
        registration.setFilter(new RedirectFilter());
        registration.addUrlPatterns("/*");
        registration.setName("RedirectFilter");
        registration.setOrder(2);
        return registration;
    }
 
}