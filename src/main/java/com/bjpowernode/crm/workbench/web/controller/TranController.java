package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/tran")
public class TranController {

    @Resource
    private TranService tranService;

    @Resource
    private ActivityService activityService;

    @Resource
    private CustomerService customerService;

    @RequestMapping("/add.do")
    @ResponseBody
    public ModelAndView add(){
        System.out.println("进入到跳转到交易添加页的操作");
        ModelAndView mv = new ModelAndView();
        List<User> uList = activityService.getUserList();
        mv.addObject("uList", uList);
        mv.setViewName("/workbench/transaction/save.jsp");
        return mv;
    }


    @RequestMapping("/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name) {
        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");
        List<String> sList = customerService.getCustomerName(name);
        return sList;
    }
}
