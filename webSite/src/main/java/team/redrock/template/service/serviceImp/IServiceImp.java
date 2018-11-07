package team.redrock.template.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.redrock.template.config.Config;
import team.redrock.template.dao.Dao;
import team.redrock.template.service.IService;
import team.redrock.template.util.Util;
import team.redrock.template.vo.Photo;
import team.redrock.template.vo.User;
import team.redrock.template.vo.Web;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class IServiceImp implements IService {
    @Autowired
    private Config config;
    @Autowired
    private Dao dao;
    @Autowired
    private IServiceImp iServiceImp;

    /**
     *
     * @param data 所有推荐网站信息
     * @return 插入到推荐文件
     */
    @Override
    public Boolean insertDataToFile(String data, String fileNameAndFather) {
        BufferedWriter writer = null;

        File file = new File(this.getClass().getResource(fileNameAndFather).getPath());

        //如果文件不存在，则新建一个
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file));
            writer = new BufferedWriter(write);
            writer.write(data);
            writer.flush();
            write.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer == null){
                    writer.close();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public String alter(String websApplication, JSONObject req) {
        return Util.alterWebs(websApplication, req);
    }

    @Override
    public String selectInfo(String fileName) {

        String Path=this.getClass().getResource("/src/main/webSite/test.txt").getPath();
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }


    @Override
    public JSONObject selectWebsByKind(String kindName) {
        Web web = new Web();

        web.setKindname(kindName);
        Set<String> set = this.dao.getTypes(web);
        List<Web> webList10 = null;
        Gson gson = new Gson();

        String info10 = null;
        JSONArray jsonArray10 = null;

        JSONArray jsonArrayWebs = new JSONArray();
        JSONObject json = new JSONObject();

        Iterator<String> it = set.iterator();
        while (it.hasNext()){
            JSONObject jsonObject = new JSONObject();
            String str = it.next();
            web.setType(str);
            webList10 = this.dao.getWebs(web);
            info10 = gson.toJson(webList10);
            jsonArray10 = JSONArray.parseArray(info10);
            jsonObject.put("type", web.getType());
            jsonObject.put("weblist", jsonArray10);
            jsonArrayWebs.add(jsonObject);

        }

        json.put("kindname", web.getKindname());
        json.put("webs", jsonArrayWebs);
        return json;
    }

    @Override
    public String setAllWebs() {
        Set<String> set = this.dao.selectKindNames();

        Iterator<String> it = set.iterator();
        JSONArray jsonArray = new JSONArray();
        while (it.hasNext()) {
            String str = it.next();
            JSONObject jsonObject = new JSONObject();
            jsonObject = this.iServiceImp.selectWebsByKind(str);
            jsonArray.add(jsonObject);
        }
        return jsonArray.toJSONString();

    }
    public Boolean insertBannerToFile(Photo photo){
        List<Photo> photoList = this.dao.selectBanner(photo);
        Gson gson = new Gson();
        String info = gson.toJson(photoList);
        this.insertDataToFile(info, config.getBanner());
        return true;
    }
    public Boolean insertAboutsToFile(Photo photo){
        List<Photo> photoList = this.dao.selectAbouts(photo);
        Gson gson = new Gson();
        String info = gson.toJson(photoList);
        this.insertDataToFile(info, config.getAbouts());
        return true;
    }

    @Override
    public Boolean insertAllWebsToFile() {
        String info = this.iServiceImp.setAllWebs();
        return this.insertDataToFile(info, config.getRecomList());
    }
    public Boolean insertHotWebsToFile(){
        List<Web> hotWebsList = this.dao.selectHotWebs();
        Gson gson = new Gson();
        String info = gson.toJson(hotWebsList);

        return this.insertDataToFile(info, config.getHotList());
    }

    @Override
    public Boolean changeWebsByKindAndType(JSONObject jsonObject) {
        Web web = new Web();
        JSONArray jsonArray = jsonObject.getJSONArray("weblist");
        String type = jsonObject.getString("type");
        web.setType(type);
        String kindname = jsonObject.getString("kindname");
        web.setKindname(kindname);

        this.dao.deleteWebs(web);
        for (int i=0; i<jsonArray.size();i++){

            jsonObject = jsonArray.getJSONObject(i);

            web.setName(jsonObject.getString("name"));
            web.setUrl(jsonObject.getString("url"));
            web.setHot(Integer.parseInt(jsonObject.getString("hot")));

            this.dao.insertWebs(web);
        }
        return true;
    }

    /**
     * 删除数据库中与参数不符合的一级／二级标题下的网站
     * @param jsonObject
     * @return
     */
    @Override
    public Boolean deleteWebsByKindName(JSONObject jsonObject) {
        Web web = new Web();
        JSONArray jsonArray = jsonObject.getJSONArray("kindnames");
        String[] kindnames = new String[jsonArray.size()];



        for (int i =0; i< jsonArray.size(); i++){
            JSONObject jsonObjectKind = jsonArray.getJSONObject(i);
            kindnames[i]= jsonObjectKind.getString("kindname");

            //参数中的kindname 与数据库中kindname一一对比，如果有相符，则查库，每一个与参数中type对比
            JSONArray typeArray = jsonObjectKind.getJSONArray("type");
            String[] TypesString = new String[typeArray.size()];
            for (int j=0; j<typeArray.size(); j++){
                TypesString[j] = typeArray.getJSONObject(j).getString("title2");
            }
            web.setKindname(kindnames[i]);
            this.deleteWebsByTypeAndKindName(web, TypesString);
        }

        //set:装有数据库中所有kindname的数组
        Set<String> set = this.dao.selectKindNames();
        Iterator<String> iterator = set.iterator();


        while (iterator.hasNext()){
            //遍历数据库中kindname
            String kind = iterator.next();
            web.setKindname(kind);

            if (!Util.isTrue(kind, kindnames)){
                //如果数据库中的kindname和传入的参数中所有kindname都没有匹配，则从数据库中删除

                this.dao.deleteKindname(web);
            }

        }
        return true;
    }

    /**
     *
     * @param web 删除满足条件的一级标签下某二级标签中的所有网站
     * @param typeOfParam   传入参数中某一级标题下的所有二级标题的无重复集合
     *
     * @return
     */
    @Override
    public Boolean deleteWebsByTypeAndKindName(Web web, String[] typeOfParam) {

        //参数中的kindname 与数据库中kindname一一对比，如果有相符，则查库，每一个与参数中type对比
        if (this.dao.selectWebFromKindName(web).size()!=0){
            Set<String> typeOfKindName = this.dao.getTypes(web);
            Iterator<String> it = typeOfKindName.iterator();
            while (it.hasNext()){
                String type = it.next();

                if (!Util.isTrue(type, typeOfParam)){
                    web.setType(type);
                    this.dao.deleteWebs(web);
                }
            }
        }

        return true;
    }

    @Override
    public Boolean updateHotWebs(JSONObject jsonObject) {
        Web web = new Web();
        this.dao.deleteAllHotWebs();
        JSONArray jsonArray = jsonObject.getJSONArray("weblist");
        for (int i =0;i<jsonArray.size();i++){
            JSONObject json = jsonArray.getJSONObject(i);
            web.setName(json.getString("name"));
            web.setUrl(json.getString("url"));
            if (json.getString("hot")!=null){
                web.setHot(Integer.parseInt(json.getString("hot")));
            }
            this.dao.insertHotWebs(web);
        }
        return true;
    }
    public Boolean updatePhotoToDataBase(JSONObject infoJSONObject){
        JSONArray webList = infoJSONObject.getJSONArray("weblist");
        Photo photo = new Photo(infoJSONObject.getString("type"));
        this.dao.deletePhoto(photo);
        for (int i=0; i<webList.size(); i++){

            JSONObject jsonObject = webList.getJSONObject(i);
            photo.setName(jsonObject.getString("name"));
            photo.setUrl(jsonObject.getString("url"));
            if (jsonObject.getString("link")!=null){
                photo.setLink(jsonObject.getString("link"));
            }
            this.dao.insertPhoto(photo);
        }
        return true;
    }

    @Override
    public boolean verifyLogin(User user) {
        User userList = this.dao.verityAccount(user);
        if (userList==null){
            return false;
        }else {
            return true;
        }
    }
}
