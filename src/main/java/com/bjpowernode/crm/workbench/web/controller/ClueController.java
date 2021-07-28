package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;

import com.bjpowernode.crm.settings.vo.User_TandMsg;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/clue")
public class ClueController {
    //之前做的（获取所有者的操作）放在了ActivityService里面，所有需要调用它
    @Resource
    private ActivityService activityService;

    @Resource
    private ClueService clueService;

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        System.out.println("取得用户所有者列表操作");
        List<User> userList = activityService.getUserList();
        //System.out.println(userList);
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


    @RequestMapping(value = "/detail.do")
    @ResponseBody
    public ModelAndView detail(String id){
        System.out.println("跳转到详细信息页");
        ModelAndView mv = new ModelAndView();
        Clue clue = clueService.detail(id);
        mv.addObject("c",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }


    @RequestMapping(value = "/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){
        System.out.println("进入获取详细信息页中市场活动列表的操作");
        List<Activity> aList = activityService.getActivityListByClueId(clueId);
        return aList;
    }


    @RequestMapping(value = "/unbund.do")
    @ResponseBody
    public User_TandMsg unbund(String id){
        System.out.println("接触关联关系的操作");
        User_TandMsg user_tandMsg = new User_TandMsg();
        Boolean flag = clueService.unbund(id);
        user_tandMsg.setSucess(flag);
        return user_tandMsg;
    }


    @RequestMapping(value = "/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByNameAndNotByClueId(String aname,String clueId){
        System.out.println("进入获取关联市场活动模态窗口中的市场活动列表的操作");
        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(aname, clueId);
        return aList;
    }


    @RequestMapping(value = "/bund.do")
    @ResponseBody
    public User_TandMsg bund(String cid, String aid){
        System.out.println("进入关联市场活动的操作");
        User_TandMsg user_tandMsg = new User_TandMsg();
        String[] aids = aid.split(",");
        Boolean flag = clueService.bund(cid,aids);
        user_tandMsg.setSucess(flag);
        return user_tandMsg;
    }


    @RequestMapping(value = "/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String aname){
        System.out.println("查询市场活动列表的操作（转换页面）");
        List<Activity> aList = activityService.getActivityListByName(aname);
        return aList;
    }

    @RequestMapping(value = "/convert.do")
    @ResponseBody
    public ModelAndView convert(HttpServletRequest request, String flag,String clueId,Tran tran){
        System.out.println("进入执行线索转换的操作");
        ModelAndView mv = new ModelAndView();

        //设置Tran中id的值，创建人的值，创建时间的值
        tran.setId(UUIDUtil.getUUID());
        //创建时间：当前系统时间
        tran.setCreateTime(DateTimeUtil.getSysTime());
        //创建人：当前登录用户
        tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        Boolean flag1 = clueService.convert(flag,clueId,tran);
        if(flag1){
            mv.setViewName("/workbench/clue/index.jsp");
        }
        return mv;
    }

}
