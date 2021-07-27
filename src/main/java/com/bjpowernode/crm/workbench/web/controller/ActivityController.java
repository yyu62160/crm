package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.vo.User_TandMsg;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.vo.ActivityPageListVO;
import com.bjpowernode.crm.workbench.vo.PaginationVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/activity")
public class ActivityController {
    @Resource
    private ActivityService service;

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        System.out.println("进入到查询模块页面中”所有者“的操作");
        List<User> userList = service.getUserList();
        //System.out.println(userList);
        return  userList;
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public User_TandMsg saveActivity(HttpServletRequest request,Activity activity){
        System.out.println("进入到添加市场活动的操作");
        User_TandMsg user_tandMsg = new User_TandMsg();
        //创建id
        String id = UUIDUtil.getUUID();
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setId(id);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        //System.out.println(activity);向后端传的数据
        Boolean flag = service.save(activity);
        user_tandMsg.setSucess(flag);
        return user_tandMsg;
    }

    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public PaginationVO pageList(ActivityPageListVO activityPageListVO){
        System.out.println("进入到查询市场活动列表的操作");
        //计算出略过的记录数,skipCount
        int pageNo = activityPageListVO.getPageNo();
        int pageSize = activityPageListVO.getPageSize();
        int skipCount = (pageNo - 1)*pageSize;
        activityPageListVO.setSkipCount(skipCount);

        //service层处理后返回一个PaginationVO<Activity>数据
        PaginationVO<Activity> vo = service.pageList(activityPageListVO);
        return vo;
    }

    @RequestMapping(value = "/delete.do")
    @ResponseBody
    public User_TandMsg deleteActivity(String id){
        System.out.println("进入到执行市场活动删除的操作");
        User_TandMsg user_tandMsg = new User_TandMsg();
        String[] idList = id.split(",");

        Boolean flag = service.delete(idList);
        user_tandMsg.setSucess(flag);
        return user_tandMsg;
    }


    @RequestMapping(value = "/getUserListAndActivity.do")
    @ResponseBody
    public Map<String,Object> getUserListAndActivity(String id){
        System.out.println("进入到修改市场活动的操作");
        /*
            uList
            a
         */
        Map<String,Object> map = service.getUserListAndActivity(id);
        return map;
    }


    @RequestMapping(value = "/update.do")
    @ResponseBody
    public  User_TandMsg update(HttpServletRequest request, Activity activity){
        System.out.println("进入点击更新市场活动之后的操作");
        User_TandMsg user_tandMsg = new User_TandMsg();
        //修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);


        Boolean flag = service.update(activity);
        user_tandMsg.setSucess(flag);
        return user_tandMsg;
    }

    @RequestMapping(value = "/detail.do")
    @ResponseBody
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        System.out.println("进入到跳转详细信息页的操作");
        Activity activity = service.detail(id);
        //System.out.println(activity);
        mv.addObject("a",activity);
        mv.setViewName("/workbench/activity/detail.jsp");
        return mv;
    }


    @RequestMapping(value = "/getRemarkListByAid.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListByAid(String activityId){
        System.out.println("进入到获取详细信息页中备注信息的操作");
        List<ActivityRemark> activityRemarkList = service.getRemarkListByAid(activityId);
        //System.out.println(activityRemarkList);
        return activityRemarkList;
    }


    @RequestMapping(value = "/deleteRemark.do")
    @ResponseBody
    public User_TandMsg deleteRemark(String id){
        System.out.println("进入到删除备注信息的操作");
        User_TandMsg user_tandMsg = new User_TandMsg();
        Boolean flag = service.deleteRemark(id);
        user_tandMsg.setSucess(flag);
        return user_tandMsg;
    }

    @RequestMapping(value = "/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(HttpServletRequest request, ActivityRemark activityRemark){
        System.out.println("进入到执行备注添加的操作");

        /*
            {"sucess":true/false,"ar":{}}
         */
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        activityRemark.setId(id);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);
        System.out.println(activityRemark);
        Boolean flag = service.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("sucess" , flag);
        map.put("ar", activityRemark);
        return map;
    }


    @RequestMapping(value = "/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(HttpServletRequest request, ActivityRemark activityRemark) {
        System.out.println("进入到执行备注添加更新的操作");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditFlag(editFlag);
        Boolean flag = service.updateRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("sucess", flag);
        map.put("ar", activityRemark);
        return map;
    }
}
