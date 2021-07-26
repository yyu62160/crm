package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //System.out.println("进入到验证有没有登录过的过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();
        //System.out.println(path);
        //不应该被拦截的资源，自动放行
        if("/login.jsp".equals(path) || "/user/queryUser.do".equals(path)){
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            //其他资源必须验证
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if(user != null){
                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }



    }
}

