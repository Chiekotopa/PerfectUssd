/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.service;

import com.ussd.pay.dao.SessiontransRepository;
import com.ussd.pay.entities.Sessiontrans;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Carlos TCHIOZEM
 */

@Service
public class MultiThread extends Thread {

    @Autowired
    SessiontransRepository sessiontransRepository;

    private String phoneDest;

  

    public void test() {
        Sessiontrans sessiontrans = new Sessiontrans();
        
       List<Sessiontrans> sessions=new ArrayList<>();
        sessions=sessiontransRepository.findAll();
        for (Sessiontrans object : sessions) {
            System.out.println(object.getCodesecret());
        }
        
    }
    
 @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Sessiontrans sessiontrans = new Sessiontrans();
        sessiontrans = sessiontransRepository.findSessiontransBySecretcode(phoneDest);
         List<Sessiontrans> sessions=new ArrayList<>();
        while ((System.currentTimeMillis() - startTime) < 65000) {
            try {

                System.out.println(System.currentTimeMillis() - startTime);
                Thread.sleep(3000);
                
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            sessions=sessiontransRepository.findListSessiontransBySecretcode(phoneDest);
                if (sessions.isEmpty()) {
                    break;
                }
        }
        sessiontrans.setCodesecret("time out");
        sessiontrans.setStatus("0");
        sessiontransRepository.save(sessiontrans);
        System.out.println("Ok");
        
       
    }
    
    
    public void setphone(String phone){
        phoneDest =phone;
    }
    public void getphone(){
        System.out.println(phoneDest);
    }
}
