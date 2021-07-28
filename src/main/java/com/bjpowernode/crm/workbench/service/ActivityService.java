package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.vo.ActivityPageListVO;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    List<User> getUserList();

    Boolean save(Activity activity);

    PaginationVO<Activity> pageList(ActivityPageListVO activityPageListVO);

    Boolean delete(String[] idList);

    Map<String,Object> getUserListAndActivity(String id);

    Boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    Boolean deleteRemark(String id);

    Boolean saveRemark(ActivityRemark activityRemark);

    Boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId);

    List<Activity> getActivityListByName(String aname);

}
