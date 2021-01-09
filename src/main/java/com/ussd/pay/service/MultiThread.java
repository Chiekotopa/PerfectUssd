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
    private String phoneExp;

    public MultiThread(SessiontransRepository sessiontransRepository) {
        this.sessiontransRepository = sessiontransRepository;
    }

    public void test() {
        Sessiontrans sessiontrans = new Sessiontrans();
        Sessiontrans session = new Sessiontrans();
        List<Sessiontrans> sessions = new ArrayList<>();
        sessions = sessiontransRepository.findAll();
        for (Sessiontrans object : sessions) {
            System.out.println(object.getMontant());
        }
        session = sessiontransRepository.findListSessiontransBySecretcode(this.phoneDest, this.phoneExp);
        System.out.println(this.phoneDest + "******************** dest");
        System.out.println(this.phoneExp + "******************** Exp");
        System.out.println(session.getStatus() + "********************");

    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println(phoneDest);
        while ((System.currentTimeMillis() - startTime) < 65000) {
            try {
                System.out.println(System.currentTimeMillis() - startTime);
                Thread.sleep(3000);
                Sessiontrans sessions = new Sessiontrans();
                sessions = sessiontransRepository.findListSessiontransBySecretcode(this.phoneDest, this.phoneExp);
                System.out.println(this.phoneDest + "******************** dest");
                System.out.println(this.phoneExp + "******************** Exp");
                System.out.println(sessions.getStatus() + "********************");

                if (sessions.getStatus().equals("2")) {
                    sessions.setThread("0");
                    sessions.setStatus("2");
                    sessiontransRepository.save(sessions);
                    break;
                }
                if (sessions.getStatus().equals("-1")) {
                    sessions.setThread("0");
                     sessions.setStatus("-1");
                    sessiontransRepository.save(sessions);
                    break;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if ((System.currentTimeMillis() - startTime) >= 65000) {
            Sessiontrans sessions = new Sessiontrans();
            sessions = sessiontransRepository.findListSessiontransBySecretcode(this.phoneDest, this.phoneExp);
            sessions.setCodesecret("time out");
            sessions.setStatus("0");
            sessions.setThread("0");
            sessiontransRepository.save(sessions);
            System.out.println("Ok");
        }

    }

    public void setphone(String phone) {
        this.phoneDest = phone;
    }

    public void setphoneExp(String phone) {
        this.phoneExp = phone;
    }

    public void getphone() {
        System.out.println(phoneDest);
    }
}
