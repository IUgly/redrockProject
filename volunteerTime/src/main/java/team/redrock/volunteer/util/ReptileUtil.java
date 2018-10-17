package team.redrock.volunteer.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.redrock.volunteer.vo.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ReptileUtil {

    public static List<Record> detail(String account, String pswd){

        List<Record> recordList = new ArrayList<>();

        String html = ImitateLogin.ImitateLogin(account,pswd);
        Document doc = Jsoup.parse(html);//解析HTML字符串返回一个Document实现
        Element content = doc.select("table").first();//查找第一个a元素
        if (content==null){
            return null;
        }
        Elements links = content.getElementsByTag("tr");

        Element linkFirst = links.first();
        Elements linkElem = linkFirst.siblingElements();
        ListIterator<Element> result = linkElem.listIterator();
        double allHours = 0.0;

        while(result.hasNext()) {
            Record record = new Record();

            Element get = result.next();
            String para = get.text();
            String[] splited = para.split("\\s+");

            int len = splited.length;
            String ZYContent = new String();
            String hours = new String(splited[0]);
            allHours = allHours+Double.parseDouble(hours);

            String start = splited[len-2]+" "+splited[len-1];
            String title = splited[len-4];
            String addWay = splited[len-6];
            String status = splited[len-5];
            String server = splited[len-3];
            for (int i =len-7; i>1 ; i--){
                ZYContent = splited[i] +" "+ZYContent;
            }
            record.JsonResponse(hours, ZYContent, start, title, addWay, status,server);
            recordList.add(record);
        }
        return recordList;
    }
}
