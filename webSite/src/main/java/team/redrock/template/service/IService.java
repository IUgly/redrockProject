package team.redrock.template.service;

import com.alibaba.fastjson.JSONObject;
import team.redrock.template.vo.User;
import team.redrock.template.vo.Web;

public interface IService {
    /*
    *将所有数据库中的推荐网站  覆盖原文件内容
    *
     */
    public Boolean insertDataToFile(String data, String fileNameAndFather);

    public String alter(String websApplication, JSONObject req);

    public String selectInfo(String fileName);

    public JSONObject selectWebsByKind(String kindName);

    public String setAllWebs();

    public Boolean insertAllWebsToFile();

    public Boolean insertHotWebsToFile();

    /*
    *根据一级二级标签修改其下所有网站
     */
    public Boolean changeWebsByKindAndType(JSONObject jsonObject);


    /**
     *  删除某一级标签下所有网站
     * @param jsonObject 接收参数 将所有kindname 装入字符串数组。
     *                   一一查找出数据库中kindname 并一一验证
     *                   如果数据库中某一kindname与数组中没有相同的
     *                   则删除该kindname
     * @return
     */
    public Boolean deleteWebsByKindName(JSONObject jsonObject);

    /**
     *
     * @param web 删除满足条件的一级标签下某二级标签中的所有网站
     * @return
     */
    public Boolean deleteWebsByTypeAndKindName(Web web, String[] typeOfParam);

    /**
     *
     * @param jsonObject  将更新热门网站数据库
     * @return
     */
    public Boolean updateHotWebs(JSONObject jsonObject);

    public boolean verifyLogin(User user);

    public Boolean updatePhotoToDataBase(JSONObject infoJSONObject);

    public String getHotWebs();

    public String getBanner();

    public String getAboutUs();


}
