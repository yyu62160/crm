package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;

import com.bjpowernode.crm.settings.vo.User_TandMsg;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/clue")
public class ClueController {
    //之前做的所有的操作放在了ActivityService里面，所有需要调用它
    @Resource
    private ActivityService activityService;

    @Resource
    private ClueService clueService;

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        System.out.println("取得用户所有者列表操作");
        List<User> userList = activityService.getUserList();
        System.out.println(userList);
        return userList;
    }


    @RequestMapping(value = "/save.do")
    @ResponseBody
    public User_TandMsg save(HttpServletRequest request, Clue clue){
        System.out.println("保存用户创建的操作");
        //创建id
        String id = UUIDUtil.getUUID();
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        clue.setId(id);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        User_TandMsg user_tandMsg = new User_TandMsg();
        Boolean flag = clueService.save(clue);
        user_tandMsg.setSucess(flag);
        return user_tandMsg;
    }
}
