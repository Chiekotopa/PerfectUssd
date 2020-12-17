/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.service;

import com.ussd.pay.dao.SessiontransRepository;
import com.ussd.pay.entities.Sessiontrans;

/**
 *
 * @author Chieko Topa
 */

public class MultiThread extends Thread {

    public SessiontransRepository sessiontransRepository;

    private String phoneDest;

    public MultiThread(SessiontransRepository sessiontransRepository) {
        this.sessiontransRepository = sessiontransRepository;
        
    }

    public void test() {
        Sessiontrans essiontrans1 = sessiontransRepository.getOne(19);
        System.out.println(essiontrans1.getMontant());
    }

    public String returnStatus(int id) {
        Sessiontrans sessiontrans1 = sessiontransRepository.getOne(id);

        return sessiontrans1.getStatus();
    }

 
    public Sessiontrans initTraitement() {
        Sessiontrans sessiontrans = new Sessiontrans();
        sessiontrans = sessiontransRepository.findBySessiontrans(phoneDest);
         System.out.println(sessiontrans.getMontant());
        return sessiontrans;
    }
    
    public void TimeOut(){
        long startTime = System.currentTimeMillis();
        Sessiontrans sessiontrans = initTraitement();
       int id;
        System.out.println(phoneDest + " thread");

        System.out.println(sessiontrans.getMontant() + " thread");
        String status;
        while ((System.currentTimeMillis() - startTime) < 65000) {
            try {

                System.out.println(System.currentTimeMillis() - startTime);
                Thread.sleep(3000);
                System.out.println(sessiontrans.getId() + " thread");
                status = returnStatus(19);

                if (status.equals("2")) {
                    break;
                }

                if (status.equals("-1")) {
                    break;
                }

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//           
        }
        sessiontrans.setCodesecret("time out");
        sessiontrans.setStatus("0");
        sessiontransRepository.save(sessiontrans);
        System.out.println("Ok");

    }

    @Override
    public void run() {
       TimeOut();
    }
    
    

    public void setphone(String phone) {
        phoneDest = phone;
    }

    public void getphone() {
        System.out.println(phoneDest);
    }
}
