package team.redrock.cheerleaders.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OpenIdFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        String openid = request.getParameter("openid");
        System.out.println(openid);

        if (session.getAttribute("openid") == null &
            openid == null){
            StringBuilder builder = new StringBuilder();
//            String url = request.getRequestURI();

            String url = "http://"
                    + request.getServerName() //服务器地址
                    + ":"
                    + request.getServerPort()           //端口号
                    + request.getContextPath()      //项目名称
                    + request.getServletPath()      //请求页面或其他地址
                    + "?" + (request.getQueryString());

            String paramStr = request.getQueryString();
            if(paramStr == null){
            }else{
                url += "&"+paramStr;
            }
            System.out.println(url);
            builder.append("https://wx.idsbllp.cn/MagicLoop/index.php?s=/addon/Api/Api/oauth");
            builder.append("&redirect=");
            builder.append(url);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendRedirect(builder.toString());
        }else{
            session.setAttribute("oepnid", openid);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
