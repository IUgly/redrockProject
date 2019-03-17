package team.redrock.cheerleaders.filter;

import team.redrock.cheerleaders.util.HttpUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RedirectFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String openid = request.getParameter("openid");
        if (openid!=null)
        {
            if (HttpUtil.judgingQualification(openid)){
                HttpSession session = request.getSession();
                session.setAttribute("openid",openid);
                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                servletResponse.getWriter().write("您没有投票资格");
            }
        }else {
            servletResponse.getWriter().write("请重新登陆,");
        }

    }

    @Override
    public void destroy() {

    }
}
