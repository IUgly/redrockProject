package team.redrock.template.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:define.properties")
public class Config {
    @Value("${webSite.upload.photo}")
    private String photo;
    @Value("${webSite.alter.recomList}")
    private String recomList;
    @Value("${webSite.alter.hotList}")
    private String hotList;
    @Value("${webSite.upload.photo.url}")
    private String photoUrl;
    @Value("${webSite.photo.banner}")
    private String banner;
    @Value("${webSite.photo.abouts}")
    private String abouts;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getAbouts() {
        return abouts;
    }

    public void setAbouts(String abouts) {
        this.abouts = abouts;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRecomList() {
        return recomList;
    }

    public void setRecomList(String recomList) {
        this.recomList = recomList;
    }

    public String getHotList() {
        return hotList;
    }

    public void setHotList(String hotList) {
        this.hotList = hotList;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
