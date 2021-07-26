package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.MyUserException;
import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User loginUsers(User user) throws MyUserException{
        User user1 = userDao.loginUsers(user);
        if(user1 == null){
            throw new LoginException("账户密码错误");
        }
        //如果程序能够成功执行到这，说明账户密码正确
        //向下继续验证其他3项信息

        //验证失效时间
        String expireTime = user1.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账户已失效");
        }

        //判断锁定状态
        String lockState = user1.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账户已锁定");
        }

        /*
        //先不判断
        //判断ip地址
        //数据库中的ip地址
        String allowIps = user1.getAllowIps();
        //用户的IP地址
        String allowIps1 = user.getAllowIps();
        //System.out.println(allowIps1);
        if(!allowIps.contains(allowIps1)){
            throw new LoginException("IP地址受限");
        }
        */
        return user1;
    }
}
