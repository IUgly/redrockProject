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
        if (session.getAttribute("openid") == null){
            StringBuilder builder = new StringBuilder();
            String url = request.getRequestURI();
            String paramStr = request.getQueryString();
            if(paramStr == null){

            }else{
                url += "&"+paramStr;
            }
            builder.append("https://wx.idsbllp.cn/MagicLoop/index.php?s=/addon/Api/Api/oauth");
            builder.append("&redirect=");
            builder.append(url);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendRedirect(builder.toString());
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
