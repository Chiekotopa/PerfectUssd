/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.service;

import com.ussd.pay.pojo.Responses;
import java.util.HashMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Carlos TCHIOZEM
 */
@Service
public class UssdService {

    HttpHeaders headers = new HttpHeaders();

    RestTemplate restTemplate = new RestTemplate();

    public void transfertToClientPerfectPay() {
        HashMap hashMap = new HashMap();

    }

    public Responses CheckCompteExpediteur(String phone) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=checker_compte_expediteur&Code_client=" + phone + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckCompteDestinaire(String phoneExp, String phoneDest) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=checker_compte_destinataire&Code_clientExpediteur=" + phoneExp + "&Code_clientDestinataire=" + phoneDest + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses Checksolde(String solde, String phoneExp,String phoneDest) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=checker_solde_expediteur_transfert&Code_clientExpediteur="+phoneExp+"&Code_clientDestinataire="+phoneDest+"&Montant="+solde+"";
        response = restTemplate.getForObject(url, Responses.class, response);
        return response;
    }
    
    public Responses validationTransfert(String solde, String phoneExp,String phoneDest,String securiteCode) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);
        

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=validation_transfert_account_perfect_pay&Code_clientExpediteur="+phoneExp+"&Code_clientDestinataire="+phoneDest+"&Montant="+solde+"&CodeSecurite="+securiteCode+"";
        response = restTemplate.getForObject(url, Responses.class, response);
        System.out.println(boString);
        return response;
    }

}
