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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ussd.pay.entities.Sessionussd;

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

    //check solde pour un transfert
    public Responses CheckSoldeTransfert(String solde, String phoneExp, String phoneDest) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=checker_solde_expediteur_transfert&Code_clientExpediteur=" + phoneExp + "&Code_clientDestinataire=" + phoneDest + "&Montant=" + solde + "";
        response = restTemplate.getForObject(url, Responses.class, response);
        return response;
    }

    public Responses validationTransfert(String solde, String phoneExp, String phoneDest, String securiteCode) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=validation_transfert_account_perfect_pay&Code_clientExpediteur=" + phoneExp + "&Code_clientDestinataire=" + phoneDest + "&Montant=" + solde + "&CodeSecurite=" + securiteCode + "";
        response = restTemplate.getForObject(url, Responses.class, response);
        System.out.println(boString);
        return response;
    }

    public Responses CheckCodeSecret(String phoneExp, String secretCode) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=affiche_solde_ussd&CodeClient=" + phoneExp + "&secret_code=" + secretCode + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckListTransation(String phoneExp, String secretCode) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=liste_transaction_ussd&CodeClient=" + phoneExp + "&secret_code=" + secretCode + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckAncienCode(String phoneExp, String secretCode) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=verifier_ancien_code_secret&CodeClient=" + phoneExp + "&secret_code=" + secretCode + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckNouveauCode(String ancienCode, String newCode) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=verifier_nouveau_code_secret&ping_code=" + newCode + "&ancien_ping_code=" + ancienCode + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses updateCodeSecret(String ancienCode, String phoneExp, String repeadCode, String pingCode) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105//apipayment/api-perfectpay.php?action=Editer_code_secret&CodeClient=" + phoneExp + "&repeat_ping_code=" + repeadCode + "&ping_code=" + pingCode + "&ancien_ping_code=" + ancienCode + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses checkerCodeMarchant(String codeMarchand) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=checker_code_Marchand_usssd&CodeMarchand=" + codeMarchand + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses checkerCodeSecret2(String codeMarchand, String codeClient, String codeSecret) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=checker_code_Marchand_usssd_verrif&CodeMarchand=" + codeMarchand + "&CodeClient=" + codeClient + "&secret_code=" + codeSecret + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses checkSoldePaiment(String codeMarchand, String codeClient, Double montant) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=checker_solde_paiement_marchand&CodeMarchand=" + codeMarchand + "&CodeClient=" + codeClient + "&Montant=" + montant + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses MakePaimentMarchand(String codeMarchand, String codeClient, Double montant, String codeSecurite) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=paiement_marchand_ussd&Code_client=" + codeClient + "&CodeMarchand=" + codeMarchand + "&Montant=" + montant + "&CodeSecurite=" + codeSecurite + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckCni(String Cni) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=Verification_CNI&NumeroCNI=" + Cni + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckSexe(String Sexe) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=Verification_Sexe&Sexe=" + Sexe + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckDate(String Date) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=Verification_DateNaissance&DateNaissance=" + Date + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckContribuable(String numContribuable) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=Verification_NumeroContribuable&NumeroContribuable=" + numContribuable + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CheckEmail(String email) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=Verification_Email&Email=" + email + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses ConfirmCodeSecret(String codeSecret, String codeRepead) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=Verification_Codes_Secrets&CodeSecret=" + codeSecret + "&CodeSecret_repeat=" + codeRepead + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses CreateAccountUssd(Sessionussd sessionUssd) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://154.72.148.105/apipayment/api-perfectpay.php?action=create_account_ussd&Nom=" + sessionUssd.getNom() + "&Prenom=" + sessionUssd.getPrenom() + "&NumeroCNI=" + sessionUssd.getCni() + "&Telephone=" + sessionUssd.getMsisdn() + ""
                + "&Sexe=" + sessionUssd.getSexe() + "&DateNaissance=" + sessionUssd.getDatenaissance() + "&LieuNaissance=" + sessionUssd.getLieunaissance() + "&NumeroContribuable=" + sessionUssd.getNumbcontribuable()
                + "&CodeSecret=" + sessionUssd.getCodesecret() + "&Email=" + sessionUssd.getEmail() + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }

    public Responses checkCodeSecretForNewAccount(String secretCode) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://localhost/apipayment/api-perfectpay.php?action=Verification_CodeSecret&CodeSecret=" + secretCode + "";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }
    
     public Responses checkCompteExpediteurMenu(String tel) {
        Responses response = new Responses();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        String boString = "";
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<Responses> entity = new HttpEntity<>(response, headers);

        String url = "http://localhost/apipayment/api-perfectpay.php?action=checker_compte_Expediteur_menu&Code_client="+tel+"";
        response = restTemplate.getForObject(url, Responses.class, response);

        return response;
    }
   

    public String replaceChaine(String chaine) {

        String originalstring = chaine;
        String replace1 = originalstring.replace('%', ' ');
        replace1 = replace1.replace('2', ' ');
        replace1 = replace1.replace('0', ' ');
        System.out.println(replace1);

        return replace1;
    }

    public String replaceChaine2(String chaine) {

        String originalstring = chaine;
        String replace1 = originalstring.replace('?','@');

        System.out.println(replace1);

        return replace1;
    }

}
