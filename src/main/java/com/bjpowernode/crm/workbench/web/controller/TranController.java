package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tran")
public class TranController {

    @Resource
    private TranService tranService;

    @Resource
    private ActivityService activityService;

    @Resource
    private CustomerService customerService;
    //全局作用域，上下文域
    @Resource
    private ServletContext application;

    @Resource
    private TranHistoryDao tranHistoryDao;

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


    @RequestMapping("/save.do")
    @ResponseBody
    public ModelAndView save(HttpServletRequest request, String customerName, Tran tran){
        //暂时只有客户的名称customerName，没有客户的id
        System.out.println("进入到添加交易的操作");
        ModelAndView mv = new ModelAndView();
        //添加id
        tran.setId(UUIDUtil.getUUID());
        //创建时间：当前系统时间
        tran.setCreateTime(DateTimeUtil.getSysTime());
        //创建人：当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        tran.setCreateBy(createBy);

        Boolean flag = tranService.save(customerName, tran);
        if(flag){
            mv.setViewName("/workbench/transaction/index.jsp");
        }
        return mv;
    }


    @RequestMapping("/detail.do")
    @ResponseBody
    public ModelAndView detail(String id){
        System.out.println("跳转到详细信息页");
        ModelAndView mv = new ModelAndView();
        Tran t = tranService.detail(id);
        //处理可能性
        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);
        mv.addObject("t",t);
        mv.setViewName("/workbench/transaction/detail.jsp");
        return mv;
    }


    @RequestMapping("/getHistoryListByTranId.do")
    @ResponseBody
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        System.out.println("进入根据交易id取交易历史列表的操作");
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);
        for(TranHistory th: thList){
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }
        return thList;
    }
}
