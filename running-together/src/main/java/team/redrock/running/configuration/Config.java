package team.redrock.running.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:define.properties")
@Data
public class Config {
    @Value("${webSite.upload.photo}")
    private String photo;
    @Value("${webSite.upload.photo.url}")
    private String photoUrl;

}
