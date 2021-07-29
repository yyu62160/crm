package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;

public interface TranService {
    Boolean save(String customerName, Tran tran);

    Tran detail(String id);

}
