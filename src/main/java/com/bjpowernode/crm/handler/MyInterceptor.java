/*package com.bjpowernode.crm.handler;
import com.bjpowernode.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器启动");
        Boolean login = false;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user != null){
            login = true;
            return login;
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return login;

    }

    //暂时用不上
}*/
