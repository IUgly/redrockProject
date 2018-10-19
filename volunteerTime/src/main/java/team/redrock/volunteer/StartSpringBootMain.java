package team.redrock.volunteer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication	// 启动SpringBoot程序，而后自带子包扫描
@EnableTransactionManagement
public class StartSpringBootMain extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(StartSpringBootMain.class) ;
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(StartSpringBootMain.class, args);
    }
}
