package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.exception.MyUserException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.vo.User_TandMsg;
import com.bjpowernode.crm.settings.vo.User_js;
import com.bjpowernode.crm.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService service;

    @RequestMapping("/queryUser.do")
    @ResponseBody
    public User_TandMsg queryUser(HttpServletRequest request ,User_js user_js) throws MyUserException {
        System.out.println("进入到登入验证的操作");
        User_TandMsg user_tandMsg = new User_TandMsg();
        User user = new User();
        String loginPwd = user_js.getLoginPwd();
        //将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        user.setLoginAct(user_js.getLoginAct());
        user.setLoginPwd(loginPwd);
        //接收客户端的ip,并赋值给user中的ip属性
        user.setAllowIps(request.getRemoteAddr());
        //调用登录方法
        User users = service.loginUsers(user);
        request.getSession().setAttribute("user", users);
        user_tandMsg.setSucess(true);
        return  user_tandMsg;
    }
}
