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

    public MultiThread(SessiontransRepository sessiontransRepository) {
        this.sessiontransRepository = sessiontransRepository;
    }

    public void test() {
        Sessiontrans sessiontrans = new Sessiontrans();

        List<Sessiontrans> sessions = new ArrayList<>();
        sessions = sessiontransRepository.findAll();
        for (Sessiontrans object : sessions) {
            System.out.println(object.getCodesecret());
        }

    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Sessiontrans sessiontrans = new Sessiontrans();
        sessiontrans = sessiontransRepository.findBySessiontrans(this.phoneDest);
        System.out.println(sessiontrans.getMontant());
        Sessiontrans sessions = new Sessiontrans();
        System.out.println(phoneDest);
        while ((System.currentTimeMillis() - startTime) < 65000) {
            try {

                System.out.println(System.currentTimeMillis() - startTime);
                Thread.sleep(3000);
                sessions = sessiontransRepository.findListSessiontransBySecretcode(this.phoneDest);

                if (sessions.getStatus().equals("2")) {
                    sessions.setThread("0");
                    sessiontransRepository.save(sessions);
                    break;
                }
                if (sessions.getStatus().equals("-1")) {
                    sessions.setThread("0");
                    sessiontransRepository.save(sessions);
                    break;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if ((System.currentTimeMillis() - startTime) >= 65000) {
            sessiontrans.setCodesecret("time out");
            sessiontrans.setStatus("0");
            sessiontransRepository.save(sessiontrans);
            System.out.println("Ok");
        }

    }

    public void setphone(String phone) {
       this.phoneDest = phone;
    }

    public void getphone() {
        System.out.println(phoneDest);
    }
}
