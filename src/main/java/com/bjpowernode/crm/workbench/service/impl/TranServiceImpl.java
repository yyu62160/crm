package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;

    @Override
    public Boolean save(String customerName, Tran tran) {
        /*
         交易添加业务：
            在做添加之前，参数t里面就少了一项信息，就是客户的主键，customerId
            先处理客户相关的需求
            1)判断customerName，根据客户名称在客户表进行精确查询
                如果有这个客户，则取出这个客户的id，封装到tran对象中
                如果没有这个客户，则在客户表新建一个客户，在从新建的客户中取出id，封装到tran对象中
            2)经过以上操作后，tran对象中的信息就全了，需要执行添加交易的操作

            3)添加交易完毕后，需要创建一条交易历史
         */
        Boolean flag = true;
        //1)判断customerName，根据客户名称在客户表进行精确查询
        Customer cus = customerDao.getCustomerByName(customerName);
        //如果没有这个客户，则在客户表新建一个客户
        if(cus == null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setCreateBy(tran.getCreateBy());
            cus.setContactSummary(tran.getContactSummary());
            cus.setNextContactTime(tran.getNextContactTime());
            cus.setOwner(tran.getOwner());
            //添加客户
            int count1 = customerDao.save(cus);
            if(count1 != 1){
                flag = false;
            }
        }

        //通过以上对于客户的处理不论是查询出来已有的客户，还是没有的客户，现在客户的id都有了
        //将客户的id封装到tran对象中
        tran.setCustomerId(cus.getId());

        //添加交易
        int count2 = tranDao.save(tran);
        if(count2 != 1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(tran.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);
        return t;
    }
}
