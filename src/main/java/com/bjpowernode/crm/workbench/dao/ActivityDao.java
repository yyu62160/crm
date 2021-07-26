package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.vo.ActivityPageListVO;

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
}
