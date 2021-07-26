package com.bjpowernode.crm.settings.vo;

public class User_TandMsg {
    private Boolean sucess;
    private String msg;

    public Boolean getSucess() {
        return sucess;
    }

    public void setSucess(Boolean sucess) {
        this.sucess = sucess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "User_TandMsg{" +
                "sucess=" + sucess +
                ", msg='" + msg + '\'' +
                '}';
    }
}
