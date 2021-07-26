package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;


public interface UserDao {
    //查询
    User loginUsers(User user);
}
