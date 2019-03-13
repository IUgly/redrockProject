package team.redrock.cheerleaders.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.redrock.cheerleaders.vo.Photo;
import team.redrock.cheerleaders.vo.User;
import team.redrock.cheerleaders.vo.Web;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mapper
@Component
public interface Dao {
    @Select("SELECT name,url FROM webs WHERE kindname = #{kindname} and type = #{type}")
    /**
     * 根据一级标题，二级标题
     *
     */
    List<Web> getWebs(Web web);
//    List<Record> getRecordList(String uid);

    /*
    根据一级标题，查出其下所有二级标题Set
    */
    @Select("Select type FROM webs WHERE kindname = #{kindname}")
    Set<String> getTypes(Web web);

    /*
    根据一级标题，二级标题删除其下所有网站
     */
    @Delete("Delete FROM webs WHERE kindname = #{kindname} and type = #{type}")
    Boolean deleteWebs(Web web);

    /*
    根据一级标题，二级标题添加网站
     */
    @Insert("INSERT into webs(kindname,name,url,hot,type) value (#{kindname},#{name},#{url},#{hot},#{type})")
    Boolean insertWebs(Web web);

    @Select("select kindname from webs")
    Set<String> selectKindNames();

    @Select("select kindname from webs")
    LinkedHashSet selectLinkHashSetKindName();

    @Select("Select * from webs where kindname = #{kindname}")
    List<Web> selectWebFromKindName(Web web);

    /**
     * 删除某一级标签下所有网站
     * @param web
     * @return
     */
    @Delete("DELETE FROM webs WHERE kindname = #{kindname}")
    Boolean deleteKindname(Web web);

    @Insert("Insert into hotwebs(name, url, hot) value (#{name},#{url},#{hot})")
    Boolean insertHotWebs(Web web);

    @Delete("Delete FROM hotwebs")
    Boolean deleteAllHotWebs();

    @Select("SELECT * From hotwebs")
    List<Web> selectHotWebs();

    @Select("SELECT * from user_info WHERE username = #{username} and password = #{password}")
    /**
     * 验证帐号密码是否正确
     *
     */
    User verityAccount(User user);


    @Insert("INSERT INTO photo(name,url,link,type) value (#{name},#{url},#{link},#{type})")
    Boolean insertPhoto(Photo photo);

    @Select("SELECT name,url from photo where type=#{type}")
    List<Photo> selectBanner(Photo photo);

    @Select("Select name,url,link from photo where type=#{type}")
    List<Photo> selectAbouts(Photo photo);

    @Delete("DELETE FROM photo where type = #{type}")
    Boolean deletePhoto(Photo photo);


}