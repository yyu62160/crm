package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.service.DicService;

import javax.annotation.Resource;

public class DicServiceImpl implements DicService {
    @Resource
    private DicValueDao dicValueDao;

    @Resource
    private DicTypeDao dicTypeDao;

}
