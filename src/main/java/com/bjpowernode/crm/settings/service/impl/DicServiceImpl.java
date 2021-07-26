package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    @Resource
    private DicValueDao dicValueDao;

    @Resource
    private DicTypeDao dicTypeDao;

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();
        //将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();
        //将字典类型列表遍历
        for(DicType dt:dtList){
            //取得每一种类型的字典类型编码
            String code = dt.getCode();
            //根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            //"List加不加都可以"
            map.put(code+"List",dvList);
        }

        return map;
    }

    public void setDicTypeDao(DicTypeDao dicTypeDao) {
        this.dicTypeDao = dicTypeDao;
    }

    public DicTypeDao getDicTypeDao() {
        return dicTypeDao;
    }

    public void setDicValueDao(DicValueDao dicValueDao) {
        this.dicValueDao = dicValueDao;
    }

    public DicValueDao getDicValueDao() {
        return dicValueDao;
    }
}
