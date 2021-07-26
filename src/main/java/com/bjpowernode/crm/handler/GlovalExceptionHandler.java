package com.bjpowernode.crm.handler;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.vo.User_TandMsg;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlovalExceptionHandler {

    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public User_TandMsg queryUserException(Exception exception){
        User_TandMsg user_tandMsg = new User_TandMsg();
        user_tandMsg.setSucess(false);
        user_tandMsg.setMsg(exception.getMessage());
        return user_tandMsg;
    }
}
