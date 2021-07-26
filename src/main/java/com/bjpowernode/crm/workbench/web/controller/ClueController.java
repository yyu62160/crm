package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

//@Controller
@RequestMapping("/clue")
public class ClueController {
    @Resource
    private ClueService clueService;

    /*@RequestMapping(value = "/.do")
    @ResponseBody
    public void getUserList(){

    }*/
}
