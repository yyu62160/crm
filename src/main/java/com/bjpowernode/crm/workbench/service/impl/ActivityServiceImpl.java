package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.vo.ActivityPageListVO;
import com.bjpowernode.crm.workbench.vo.PaginationVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao dao;

    @Resource
    private ActivityRemarkDao activityRemarkDao;


    @Override
    public List<User> getUserList() {
        List<User> users = dao.getUserList();
        return users;
    }

    @Override
    public Boolean save(Activity activity) {
        Boolean flag = false;
        int num = dao.save(activity);
        if(num == 1){
            flag = true;
            return flag;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(ActivityPageListVO activityPageListVO) {
        //取得total
        int total = dao.getTotalByCondition(activityPageListVO);
        //取得dataList
        List<Activity> dataList = dao.getActivityListByCondition(activityPageListVO);
        //创建一个VO对象，将tatal和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        //将vo返回
        return vo;
    }

    @Override
    public Boolean delete(String[] idList) {
        Boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(idList);
        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(idList);

        if(count1!=count2){
            flag = false;
        }
        //删除市场活动
        int count3 = dao.delete(idList);
        if(count3 != idList.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public  Map<String,Object> getUserListAndActivity(String id) {
        //取uList
        List<User> uList = dao.getUserList();
        //取a
        Activity a = dao.getById(id);
        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);
        //返回map就可以了
        return map;
    }

    @Override
    public Boolean update(Activity activity) {
        Boolean flag = false;
        int num = dao.update(activity);
        if(num == 1){
            flag = true;
            return flag;
        }
        return flag;

    }

    @Override
    public Activity detail(String id) {
        Activity detail = dao.detail(id);
        return detail;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> activityRemarkList = activityRemarkDao.getRemarkListByAid(activityId);
        return activityRemarkList;
    }

    @Override
    public Boolean deleteRemark(String id) {
        Boolean flag = true;
        int count = activityRemarkDao.deleteById(id);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean saveRemark(ActivityRemark activityRemark) {
        Boolean flag = true;
        int count = activityRemarkDao.saveRemark(activityRemark);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean updateRemark(ActivityRemark activityRemark) {
        Boolean flag = true;
        int count = activityRemarkDao.updateRemark(activityRemark);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList = dao.getActivityListByClueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId) {
        List<Activity> aList = dao.getActivityListByNameAndNotByClueId(aname,clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> aList = dao.getActivityListByName(aname);
        return aList;
    }
}
