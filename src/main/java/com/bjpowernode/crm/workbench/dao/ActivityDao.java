package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.vo.ActivityPageListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityDao {
    List<User> getUserList();

    int save(Activity activity);

    List<Activity> getActivityListByCondition(ActivityPageListVO activityPageListVO);

    int getTotalByCondition(ActivityPageListVO activityPageListVO);

    int delete(String[] idList);

    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(@Param("name") String aname, @Param("clueId") String clueId);

    List<Activity> getActivityListByName(String aname);
}

