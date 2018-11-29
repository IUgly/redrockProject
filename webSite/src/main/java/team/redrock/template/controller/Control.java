package team.redrock.template.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import team.redrock.template.WebSecurityConfig;
import team.redrock.template.config.Config;
import team.redrock.template.service.serviceImp.IServiceImp;
import team.redrock.template.util.AbstractBaseController;
import team.redrock.template.util.Util;
import team.redrock.template.vo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

@RestController
public class Control extends AbstractBaseController {
    @Autowired
    private IServiceImp iServiceImp;
    @Autowired
    private Config config;

    @PostMapping(value = "/edit",produces = "application/json")
    public String index(@RequestBody JSONObject json){
        String title = json.getString("title");
        switch (title){
            case "recomweblist":this.iServiceImp.changeWebsByKindAndType(json);break;
            case "hotweblist":this.iServiceImp.updateHotWebs(json);break;
            case "recomlist":this.iServiceImp.deleteWebsByKindName(json);break;
            case "picture":this.iServiceImp.updatePhotoToDataBase(json);break;
            default:return Util.assembling(-1, "error request", "");
        }
        return Util.assembling(0,"success","");
    }
    @ResponseBody
    @GetMapping(value = "/index", produces = "application/json")
    public String getIndex(String title){
        String info = "";
        switch (title){
            case "recommend":info=this.iServiceImp.setAllWebs();break;
            case "hotwebs":info=this.iServiceImp.getHotWebs();break;
            case "aboutus":info=this.iServiceImp.getAboutUs();break;
            case "banner":info=this.iServiceImp.getBanner();break;
            default: return Util.assembling(1, "request fail", info);
        }
        return info;
    }

    @ResponseBody
    @PostMapping(value = "/upload", produces = "application/json")
    public String upload(String name, HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) { // 如果你现在是MultipartHttpServletRequest对象
            MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = mrequest.getFiles("file");
            Iterator<MultipartFile> iter = files.iterator();
            while (iter.hasNext()) {
                MultipartFile file = iter.next() ;
                if (file != null) { // 现在有文件上传
                    try {
                        // Get the file and save it somewhere
                        byte[] bytes = file.getBytes();
                        long fileName = System.currentTimeMillis();
                        Path path = Paths.get(this.config.getPhoto() +"/"+ fileName+".jpg");
                        String url = config.getPhotoUrl()+"/"+fileName+".jpg";

                        Files.write(path, bytes);
                        return Util.assembling(0, url, "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return Util.assembling(-1, "没传文件？", "");
    }

    @GetMapping(value = "/login", produces = "application/json")
    public String login(){
        return Util.assembling(-2, "用户没有登陆", "");
    }

    @PostMapping(value = "/loginVerify", produces = "application/json")
    public String loginVerify(@RequestBody User user, HttpSession session){
        boolean verify = this.iServiceImp.verifyLogin(user);
        if (verify) {
            session.setAttribute(WebSecurityConfig.SESSION_KEY, user.getUsername());
            return Util.assembling(0, "login success", "");
        } else {
            return Util.assembling(-1, "帐号密码错误", "");
        }
    }

    @GetMapping(value = "/logout", produces = "application/json")
    public String logout(HttpSession session){
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "loginOut";
    }
}
