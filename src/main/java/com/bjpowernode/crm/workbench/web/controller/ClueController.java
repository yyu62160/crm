package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;

import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/clue")
public class ClueController {
    //之前做的所有的操作放在了ActivityService里面，所有需要调用它
    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        System.out.println("取得用户所有者列表操作");
        List<User> userList = activityService.getUserList();
        System.out.println(userList);
        return userList;
    }
}
