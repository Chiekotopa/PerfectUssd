/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.pojo;

/**
 *
 * @author Carlos TCHIOZEM
 */
public class Responses {
    private Integer succes;
    private String msg;

    public Responses() {
    }

    public Integer getSucces() {
        return succes;
    }

    public void setSucces(Integer succes) {
        this.succes = succes;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
}
