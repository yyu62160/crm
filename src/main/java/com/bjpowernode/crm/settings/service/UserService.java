package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.MyUserException;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    //查询
    User loginUsers(User user) throws MyUserException;
}
