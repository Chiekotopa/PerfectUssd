/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.controler;

import com.ussd.pay.dao.SessionUssdRepository;
import com.ussd.pay.dao.SessiontransRepository;
import com.ussd.pay.dao.WaletbancaireRepository;
import com.ussd.pay.dao.WaletmobilRepository;
import com.ussd.pay.entities.Sessiontrans;
import com.ussd.pay.entities.Sessionussd;
import com.ussd.pay.entities.Waletmobilegimac;
import com.ussd.pay.pojo.PojoUssd;
import com.ussd.pay.pojo.Responses;
import com.ussd.pay.service.MultiThread;
import com.ussd.pay.service.UssdService;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Carlos TCHIOZEM
 */
@RestController
@CrossOrigin(origins = "*")

public class UssdRestController {

    @Autowired
    UssdService ussdservice;

    @Autowired
    SessionUssdRepository ussdRepository;

    @Autowired
    SessiontransRepository sessiontransRepository;

    @Autowired
    WaletbancaireRepository waletbancaireRepository;

    @Autowired
    WaletmobilRepository waletmobilRepository;

    @Autowired
    MultiThread multiThread1;

    @RequestMapping(value = "/ussd", method = RequestMethod.POST)
    public HashMap getUssd(@RequestBody PojoUssd pojoUssd) {
        HashMap map = new HashMap();

        Sessionussd sessionussd = new Sessionussd();
        Sessiontrans sessiontrans = new Sessiontrans();
        Responses response = new Responses();

        try {
            System.out.println("passe *****************************************6");
            if (ussdRepository.findApiBySessionId(pojoUssd.getSessionid()) == null) {

                if (pojoUssd.getMessage().equals("237*100")) {
                    System.out.println("passe *****************************************5");
                    if (sessiontransRepository.findBySessiontrans(pojoUssd.getMsisdn()) != null) {
                        System.out.println("passe *****************************************4");

                        sessiontrans = sessiontransRepository.findBySessiontrans(pojoUssd.getMsisdn());
                        response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                        if (response.getSucces() == 2) {
                            map.put("message", response.getMsg());
                            map.put("command", "0");
                            return map;
                        }

                        response = ussdservice.checkerSiRetraitEnCours(pojoUssd.getMsisdn());

                        if (response.getSucces() == -1) {
                            map.put("message", response.getMsg());
                            System.out.println(response.getSucces());
                            map.put("command", "1");
                            return map;
                        }

                        if (response.getSucces() == -2) {
                            map.put("message", response.getMsg());
                            map.put("command", "1");
                            System.out.println(response.getSucces());
                            return map;
                        }

                        map.put("message", response.getMsg());
                        map.put("command", "1");
                        sessiontrans.setStatus("5");
                        sessiontrans.setCodesecret("wait");
                        sessiontransRepository.save(sessiontrans);
                        return map;
                    }
                    response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                    response = ussdservice.checkCompteExpediteurMenu(pojoUssd.getMsisdn());
                    System.out.println(response.getSucces());
                    if (response.getSucces() == 1) {
                        sessionussd = new Sessionussd();
                        sessionussd.setMessage(pojoUssd.getMessage());
                        sessionussd.setMsisdn(pojoUssd.getMsisdn());
                        sessionussd.setProvider(pojoUssd.getProvider());
                        sessionussd.setSessionid(pojoUssd.getSessionid());
                        sessionussd.setAccess("0");
                        sessionussd.setLastsep("237*100");
                        sessionussd.setType("1");
                        sessionussd.setDate(new Date(System.currentTimeMillis()));
                        ussdRepository.save(sessionussd);
                        map.put("message", "Bienvenue sur PerfectPay Revendeur~1.Crediter un compte~2.Debiter un compte~3.Mon compte~4.Recharger Carte UBA~0.Annuler ");
                        map.put("command", "1");
                        return map;
                    }

                    if (response.getSucces() == 2) {
                        sessionussd = new Sessionussd();
                        sessionussd.setMessage(pojoUssd.getMessage());
                        sessionussd.setMsisdn(pojoUssd.getMsisdn());
                        sessionussd.setProvider(pojoUssd.getProvider());
                        sessionussd.setSessionid(pojoUssd.getSessionid());
                        sessionussd.setAccess("0");
                        sessionussd.setLastsep("237*100");
                        sessionussd.setType("2");
                        sessionussd.setDate(new Date(System.currentTimeMillis()));
                        ussdRepository.save(sessionussd);
                        map.put("message", "Bienvenue sur PerfectPay Client~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon Compte~6.Retrait Perfectpay~7.GimacPay~0.Annuler ");
                        map.put("command", "0");
                        return map;
                    }
                    if (response.getSucces() == 4) {
                        sessionussd = new Sessionussd();
                        sessionussd.setMessage(pojoUssd.getMessage());
                        sessionussd.setMsisdn(pojoUssd.getMsisdn());
                        sessionussd.setProvider(pojoUssd.getProvider());
                        sessionussd.setSessionid(pojoUssd.getSessionid());
                        sessionussd.setAccess("0");
                        sessionussd.setLastsep("237*100");
                        sessionussd.setType("4");
                        sessionussd.setDate(new Date(System.currentTimeMillis()));
                        ussdRepository.save(sessionussd);
                        map.put("message", "Bienvenue sur PerfectPay Agent~1.Crediter un compte PerpectPay~2.Debiter un compte PerfectPay~3.Mon compte~4.Destockage~5.Recharger Carte UBA~0.Annuler ");
                        map.put("command", "1");
                        return map;
                    }

                    if (response.getSucces() == 8) {
                        sessionussd = new Sessionussd();
                        sessionussd.setMessage(pojoUssd.getMessage());
                        sessionussd.setMsisdn(pojoUssd.getMsisdn());
                        sessionussd.setProvider(pojoUssd.getProvider());
                        sessionussd.setSessionid(pojoUssd.getSessionid());
                        sessionussd.setAccess("0");
                        sessionussd.setLastsep("237*100");
                        sessionussd.setType("8");
                        sessionussd.setDate(new Date(System.currentTimeMillis()));
                        ussdRepository.save(sessionussd);
                        map.put("message", "Bienvenue sur PerfectPay Marchand~1.Mon Compte~2.Encaissement~3.Service Tiers~0.Annuler ");
                        map.put("command", "1");
                        return map;
                    }

                    if (response.getSucces() == -1) {
                        sessionussd = new Sessionussd();
                        sessionussd.setMessage(pojoUssd.getMessage());
                        sessionussd.setMsisdn(pojoUssd.getMsisdn());
                        sessionussd.setProvider(pojoUssd.getProvider());
                        sessionussd.setSessionid(pojoUssd.getSessionid());
                        sessionussd.setAccess("1");
                        sessionussd.setLastsep("237*100");
                        sessionussd.setType("-1");
                        ussdRepository.save(sessionussd);
                        map.put("message", response.getMsg());
                        map.put("command", "1");
                        return map;
                    }

                    if (response.getSucces() == -2) {
                        map.put("message", response.getMsg());
                        map.put("command", "1");
                        return map;
                    }
                }
            }

            if (pojoUssd.getMessage().equals("2") && sessiontransRepository.findSessiontransBySecretcode(pojoUssd.getMsisdn()) != null) {
                sessiontrans = sessiontransRepository.findSessiontransBySecretcode(pojoUssd.getMsisdn());
                sessiontrans.setStatus("-1");
                sessiontrans.setCodesecret("Cancel");
                sessiontransRepository.save(sessiontrans);
                map.put("message", "Operation annuler");
                map.put("command", "0");
                return map;
            }

            //retrait d'argent du client  ---------------
            System.out.println("passe *****************************************1");
            if (sessiontransRepository.findSessiontransBySecretcode(pojoUssd.getMsisdn()) != null) {
                System.out.println("passe *****************************************2");
                sessiontrans = sessiontransRepository.findSessiontransBySecretcode(pojoUssd.getMsisdn());
                System.out.println("passe *****************************************10");
                response = ussdservice.validationRetraitAccountPerfectPay(pojoUssd.getMsisdn(), sessiontrans.getPhoneagent(), sessiontrans.getMontant(), pojoUssd.getMessage());
                System.out.println("passe *****************************************11");
                System.out.println(response.getSucces());

                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg() + "0.Annuler  ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + " ");
                    map.put("command", "0");
                    return map;
                }

                if (response.getSucces() == 1) {
                    System.out.println("passe *****************************************12");
                    sessiontrans.setStatus("2");
                    sessiontrans.setTread("0");
                    sessiontrans.setCodesecret("OK");
                    sessiontransRepository.save(sessiontrans);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }

            //retrait d'argent du client par le marchand  ---------------
            System.out.println("passe *****************************************21");
            if (sessiontransRepository.findSessiontransBySecretcode2(pojoUssd.getMsisdn()) != null) {
                System.out.println("passe *****************************************22");
                sessiontrans = sessiontransRepository.findSessiontransBySecretcode2(pojoUssd.getMsisdn());
                System.out.println("passe *****************************************23");
                response = ussdservice.check_CodeSecret(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                System.out.println("passe *****************************************24");
                System.out.println(response.getSucces());

                if (response.getSucces() == -1) {

                    map.put("message", response.getMsg() + " ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + " ");
                    map.put("command", "0");
                    return map;
                }
                if (response.getSucces() == 1) {
                    System.out.println("passe *****************************************25");
                    ussdservice.create_transaction(sessiontrans);
                    sessiontrans.setStatus("2");
                    sessiontrans.setTread("0");
                    sessiontrans.setCodesecret("OK");
                    sessiontransRepository.save(sessiontrans);

                    map.put("message", "Paiement effectue avec succes ");
                    map.put("command", "0");
                    return map;
                }
            }

            sessionussd = new Sessionussd();
            sessionussd = ussdRepository.findApiBySessionId(pojoUssd.getSessionid());

            /**
             * Gestion du compte Marchand*
             */
            //Gestion du compte Marchand***************************************************************************************************************
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Consulter le solde~2.Historique des transactions~3.Modifier le code secret~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Historique transactions Marchand----------------------------------------------------------------
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("8")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*1*2");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code ping pour afficher la liste des transactions du Marchand
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*1*2") && sessionussd.getType().equals("8")) {
                response = new Responses();
                response = ussdservice.CheckListTransation(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 22) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //retour sous menu
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*2") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Consulter le solde~2.Historique des transactions~3.Modifier le code secret~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Modifier Code Secret du Marchand-----------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("8")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*1*3");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre ancien code ping~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //etape  pour verifier le nouveau code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*1*3") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() == null && sessionussd.getType().equals("8")) {
                response = new Responses();
                response = ussdservice.CheckNouveauCode(sessionussd.getCodesecret(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setNewcode(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Confirmer le nouveau code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }
            //etape  pour confirmer le code secret Marchand
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*3") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() != null && sessionussd.getType().equals("8")) {
                response = new Responses();
                response = ussdservice.updateCodeSecret(sessionussd.getCodesecret(), pojoUssd.getMsisdn(), pojoUssd.getMessage(), sessionussd.getNewcode());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -5) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg() + "~0.Retour ");
                map.put("command", "0");
                return map;
            }

            //Checker l'ancient code secret Ussd Marchand
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*1*3") && sessionussd.getType().equals("8")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                response = ussdservice.CheckAncienCode(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setCodesecret(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le nouveau code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }
            //retour sous menu
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*3") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Consulter le solde~2.Historique des transactions~3.Modifier le code secret~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Consulter le solde Marchand------------------------------------------------------------------------------------------------------                     
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("8")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*1*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*1*1") && sessionussd.getType().equals("8")) {
                response = new Responses();
                response = ussdservice.CheckCodeSecret(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //retour sous menu
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*1") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Consulter le solde~2.Historique des transactions~3.Modifier le code secret~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100*2");
                ussdRepository.save(sessionussd);
                map.put("message", "Choisir le mode de paiement~1.PerfectPay~2.MTN Mobile Money~3.Orange Money~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("8")) {

                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*2*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer le montant~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100*3");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Payer un abonnement CANALSAT~2.Payer votre facture ENEO~3.CAMWATER~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //retour sous menu marchand------------------------------
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);
                map.put("message", "Bienvenue sur PerfectPay Marchand~1.Mon Compte~2.Encaissement~3.Service Tiers~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);
                map.put("message", "Bienvenue sur PerfectPay Marchand~1.Mon Compte~2.Encaissement~3.Service Tiers~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("8")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);
                map.put("message", "Bienvenue sur PerfectPay Marchand~1.Mon Compte~2.Encaissements~3.Services Tiers~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            /**
             * gestion du compte Call Box ou agent*
             */
            //gestion du compte Call Box ou agent****************************************************************************************************************        
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("4")) {
                sessionussd.setLastsep("237*100*3");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Consulter le solde~2.Historique des transactions~3.Modifier le code ping~4.Commissions journaliere~5.Consulter mon code point de vente~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Consulter mon code point de vente---------------------------------------------------------------  
            if (pojoUssd.getMessage().equals("5") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*5");
                ussdRepository.save(sessionussd);
                response = new Responses();
                response = ussdservice.Afficher_CodePointVente(pojoUssd.getMsisdn());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                map.put("message", response.getMsg());
                map.put("command", "1");
                return map;
            }

            //Commissions journaliere call box------------------------------------------------------  
            if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*4");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code secret et affichage des commissions call box
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*4") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.View_comissions_revendeurs_journaliere(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //Consulter le solde call box------------------------------------------------------                     
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*1") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.CheckCodeSecret(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //Historique transactions Call boxeur----------------------------------------------------------------
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*2");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code ping pour afficher la liste des transactions du Call box
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*2") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.CheckListTransation(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //Modifier Code PIN du Call Box----------------------------------------------------
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*3");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre ancien code ping~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //etape  pour verifier le nouveau code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*3") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() == null && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.CheckNouveauCode(sessionussd.getCodesecret(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setNewcode(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Confirmer le nouveau code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }
            //etape  pour confirmer le code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*3") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() != null && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.updateCodeSecret(sessionussd.getCodesecret(), pojoUssd.getMsisdn(), pojoUssd.getMessage(), sessionussd.getNewcode());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -5) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg() + "~0.Retour ");
                map.put("command", "0");
                return map;
            }

            //Checker l'ancient code secret Ussd
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*3") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.CheckAncienCode(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setCodesecret(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le nouveau code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //crediter le compte perfectPay du client par le Call Box ------------------------------------------------------------
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

                sessionussd.setLastsep("237*100*1");
                sessionussd.setAccess("phone");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le numero de telephone~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //checker le phone du destinataire
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("4") && sessionussd.getAccess().equals("phone")) {
                Responses responses = new Responses();

                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                responses = ussdservice.checkerCompteDestinataireClientUSSD(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (responses.getSucces() == -2) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -1) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -3) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("solde");
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le montant~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //checker le montant
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("4") && sessionussd.getAccess().equals("solde")) {
                Responses responses = new Responses();
                responses = ussdservice.VerificationCheckingDepotCllientUSSSD(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), Double.parseDouble(pojoUssd.getMessage()));
                if (responses.getSucces() == -5) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("securite");
                sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                ussdRepository.save(sessionussd);
                map.put("message", responses.getMsg());
                map.put("command", "1");
                return map;
            }

            //Validation de la transaction
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("4") && sessionussd.getAccess().equals("securite")) {
                Responses responses = new Responses();
                sessiontrans = new Sessiontrans();
                responses = ussdservice.validationDepotComptePerfectPayClientUSSD(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), sessionussd.getMontant(), pojoUssd.getMessage());
                if (responses.getSucces() == -6) {
                    map.put("message", responses.getMsg());
                    map.put("command", "0");
                    return map;
                }

                if (responses.getSucces() == -22) {
                    map.put("message", responses.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setAccess("validation");
                ussdRepository.save(sessionussd);
                map.put("message", responses.getMsg());
                map.put("command", "0");
                return map;
            }

            //Debitez le compte d'un client par le Call Box---------------------------
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("4")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setLastsep("237*100*2");
                sessionussd.setAccess("phone");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le numero de telephone~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //etape checker le phone du destinataire
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("4") && sessionussd.getAccess().equals("phone")) {
                Responses responses = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                responses = ussdservice.checkerCompteClientetrait(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (responses.getSucces() == -2) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -1) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("solde");
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le montant~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //etape checker le montant
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("4") && sessionussd.getAccess().equals("solde")) {
                Responses responses = new Responses();
                responses = ussdservice.checkerSoldeExpediteurRetrait(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), Double.parseDouble(pojoUssd.getMessage()));
                if (responses.getSucces() == -2) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("securite");
                sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                ussdRepository.save(sessionussd);
                map.put("message", responses.getMsg());
                map.put("command", "1");
                return map;
            }

            //etape Validation de la transaction
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("4") && sessionussd.getAccess().equals("securite")) {
                Responses responses = new Responses();
                sessiontrans = new Sessiontrans();
                responses = ussdservice.validationInitilisationRretraitAccountPerfectPay(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), sessionussd.getMontant(), pojoUssd.getMessage());
                if (responses.getSucces() == -6) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (responses.getSucces() == -22) {
                    map.put("message", responses.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setAccess("validation");
                sessiontrans.setMontant(sessionussd.getMontant());
                sessiontrans.setPhoneagent(pojoUssd.getMsisdn());
                sessiontrans.setPhonedestinataire("237" + sessionussd.getDestinataire());
                sessiontrans.setStatus("1");
                sessiontrans.setTread("1");
                sessiontrans.setType("0");
                sessiontrans.setDate(new Date(System.currentTimeMillis()));
                sessiontransRepository.save(sessiontrans);
                ussdRepository.save(sessionussd);
                MultiThread multiThread = new MultiThread(sessiontransRepository);
                multiThread.setphone("237" + sessionussd.getDestinataire());
                multiThread.setphoneExp(pojoUssd.getMsisdn());
                multiThread.start();
                map.put("message", responses.getMsg());
                map.put("command", "0");

                return map;
            }

            /**
             * *Gestions du compte revendeur*
             */
            // Gestions du compte revendeur ****************************************************************************************************************
            // Sous menu Crediter le compte pour Reventdeur ...........................................................
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("1")) {
                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Crediter un Compte Client~2.Crediter un Compte Agent~0.Annuler ");
                map.put("command", "1");
                return map;
            }
            // Sous menu Debiter le compte pour Reventdeur ...........................................................
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("1")) {
                sessionussd.setLastsep("237*100*2");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Debiter un Compte Client~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            // Sous Menu Gestion de mon Compte pour le revendeur ...............................................................
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("1")) {
                sessionussd.setLastsep("237*100*3");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Consulter le solde~2.Historique des transactions~3.Modifier le code ping~4.Commissions journaliere~5.Consulter mon code point de vente~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Consulter mon code point de vente---------------------------------------------------------------  
            if (pojoUssd.getMessage().equals("5") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*5");
                ussdRepository.save(sessionussd);
                response = new Responses();
                response = ussdservice.Afficher_CodePointVente(pojoUssd.getMsisdn());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                map.put("message", response.getMsg());
                map.put("command", "1");
                return map;
            }

            //Commissions journaliere revendeur---------------------------------------------------------------  
            if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*4");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code secret et affichage des commissions revendeur
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*4") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.View_comissions_revendeurs_journaliere(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //Consulter le solde revendeur------------------------------------------------------                     
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*1") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.CheckCodeSecret(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //Historique transactions revendeurs----------------------------------------------------------------
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*2");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code ping pour afficher la liste des transactions du revendeur
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*2") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.CheckListTransation(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //Modifier Code PIN du Revendeur----------------------------------------------------
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*3");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre ancien code ping~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //etape  pour verifier le nouveau code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*3") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() == null && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.CheckNouveauCode(sessionussd.getCodesecret(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setNewcode(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Confirmer le nouveau code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //etape  pour confirmer le code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*3") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() != null && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.updateCodeSecret(sessionussd.getCodesecret(), pojoUssd.getMsisdn(), pojoUssd.getMessage(), sessionussd.getNewcode());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -5) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg() + "~0.Retour ");
                map.put("command", "0");
                return map;
            }

            //Checker l'ancient code secret Ussd
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*3*3") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.CheckAncienCode(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setCodesecret(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le nouveau code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Crediter le compte agent----------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setLastsep("237*100*1*2");
                sessionussd.setAccess("phone");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le numero de telephone~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //checker le phone du destinataire
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*2") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("phone")) {
                Responses responses = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                responses = ussdservice.checker_compte_destinataire_agent(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (responses.getSucces() == -2) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -1) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -3) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -4) {
                    map.put("message", responses.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setAccess("solde");
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le montant~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //checker le montant
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*2") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("solde")) {
                Responses responses = new Responses();
                responses = ussdservice.Verification_Checking_agent_USSD(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), Double.parseDouble(pojoUssd.getMessage()));
                if (responses.getSucces() == -5) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("securite");
                sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                ussdRepository.save(sessionussd);
                map.put("message", responses.getMsg());
                map.put("command", "1");
                return map;
            }

            //Validation de la transaction
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*2") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("securite")) {
                Responses responses = new Responses();
                sessiontrans = new Sessiontrans();
                responses = ussdservice.validation_depot_compte_agent_USSD(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), sessionussd.getMontant(), pojoUssd.getMessage());
                if (responses.getSucces() == -6) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (responses.getSucces() == -22) {
                    map.put("message", responses.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setAccess("validation");
                ussdRepository.save(sessionussd);
                map.put("message", responses.getMsg());
                map.put("command", "0");

                return map;
            }

            //Crediter le compte client------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("1")) {

                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setLastsep("237*100*1*1");
                sessionussd.setAccess("phone");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le numero de telephone~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //checker le phone du destinataire
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*1") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("phone")) {
                Responses responses = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                responses = ussdservice.checkerCompteDestinataireClientUSSD(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (responses.getSucces() == -2) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -1) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -3) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("solde");
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le montant~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            // etape checker le montant
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*1") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("solde")) {
                Responses responses = new Responses();
                responses = ussdservice.VerificationCheckingDepotCllientUSSSD(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), Double.parseDouble(pojoUssd.getMessage()));
                if (responses.getSucces() == -5) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("securite");
                sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                ussdRepository.save(sessionussd);
                map.put("message", responses.getMsg());
                map.put("command", "1");
                return map;
            }

            //etape Validation de la transaction
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*1") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("securite")) {
                Responses responses = new Responses();
                sessiontrans = new Sessiontrans();
                responses = ussdservice.validationDepotComptePerfectPayClientUSSD(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), sessionussd.getMontant(), pojoUssd.getMessage());
                if (responses.getSucces() == -6) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (responses.getSucces() == -22) {
                    map.put("message", responses.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setAccess("validation");
                ussdRepository.save(sessionussd);
                map.put("message", responses.getMsg());
                map.put("command", "0");

                return map;
            }

            //Debitez le compte d'un client---------------------------
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("1")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

                sessionussd.setLastsep("237*100*2*1");
                sessionussd.setAccess("phone");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le numero de telephone~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //etape checker le phone du destinataire
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2*1") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("phone")) {
                Responses responses = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                responses = ussdservice.checkerCompteClientetrait(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (responses.getSucces() == -2) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (responses.getSucces() == -1) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("solde");
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le montant~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //etape checker le montant
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2*1") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("solde")) {
                Responses responses = new Responses();
                responses = ussdservice.checkerSoldeExpediteurRetrait(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), Double.parseDouble(pojoUssd.getMessage()));
                if (responses.getSucces() == -2) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("securite");
                sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                ussdRepository.save(sessionussd);
                map.put("message", responses.getMsg());
                map.put("command", "1");
                return map;
            }

            //etape Validation de la transaction
            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2*1") && sessionussd.getType().equals("1") && sessionussd.getAccess().equals("securite")) {
                Responses responses = new Responses();
                sessiontrans = new Sessiontrans();
                responses = ussdservice.validationInitilisationRretraitAccountPerfectPay(pojoUssd.getMsisdn(), sessionussd.getDestinataire(), sessionussd.getMontant(), pojoUssd.getMessage());
                if (responses.getSucces() == -6) {
                    map.put("message", responses.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (responses.getSucces() == -22) {
                    map.put("message", responses.getMsg());
                    map.put("command", "0");
                    return map;
                }
                sessionussd.setAccess("validation");
                sessiontrans.setMontant(sessionussd.getMontant());
                sessiontrans.setPhoneagent(pojoUssd.getMsisdn());
                sessiontrans.setPhonedestinataire("237" + sessionussd.getDestinataire());
                sessiontrans.setStatus("1");
                sessiontrans.setTread("1");
                sessiontrans.setType("0");
                sessiontrans.setDate(new Date(System.currentTimeMillis()));
                sessiontransRepository.save(sessiontrans);
                ussdRepository.save(sessionussd);
                MultiThread multiThread = new MultiThread(sessiontransRepository);
                multiThread.setphone("237" + sessionussd.getDestinataire());
                multiThread.setphoneExp(pojoUssd.getMsisdn());
                multiThread.start();
                map.put("message", responses.getMsg());
                map.put("command", "0");

                return map;
            }

            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("1")) {
                sessionussd.setLastsep("237*100*3");
                ussdRepository.save(sessionussd);
                map.put("message", "1.Consulter votre solde~2.Liste des transactions~3.Modifier de code PIN~4.Changer de langue~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Gestion des retours de menu
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("1")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);
                map.put("message", "Bienvenue sur PerfectPay~1.Crediter un compte PerpectPay~2.Debiter un compte PerfectPay~3.Mon compte~4.Destockage~5.Recharger Carte UBA~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("1")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);
                map.put("message", "Bienvenue sur PerfectPay~1.Crediter un compte PerpectPay~2.Debiter un compte PerfectPay~3.Mon compte~4.Destockage~5.Recharger Carte UBA~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("1")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);
                map.put("message", "Bienvenue sur PerfectPay~1.Crediter un compte PerpectPay~2.Debiter un compte PerfectPay~3.Mon compte~4.Destockage~5.Recharger Carte UBA~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            /**
             * Gestion du compte Client*
             */
            //Gestion du compte Client *****************************************************************************************************************************
            //Creation de compte ----------------------------------------------------------------------------------------------------------               
            //retour au menu
            if (pojoUssd.getMessage().equals("0") && !sessionussd.getAccess().equals("1") && sessionussd.getType().equals("-1")) {
                sessionussd.setAccess("0");
                ussdRepository.save(sessionussd);
                map.put("message", "Operation annulee!");
                map.put("command", "0");
                return map;

            }

            if (pojoUssd.getMessage().equals("1") && sessionussd.getAccess().equals("1") && sessionussd.getType().equals("-1")) {
                sessionussd.setAccess("nom");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le nom~0.Annuler ");
                map.put("command", "1");
                return map;
            }
            //Save nom 
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("nom") && sessionussd.getType().equals("-1")) {

                sessionussd.setNom(ussdservice.replaceChaine(pojoUssd.getMessage()));
                sessionussd.setAccess("prenom");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le prenom~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save prenom
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("prenom") && sessionussd.getType().equals("-1")) {

                sessionussd.setPrenom(ussdservice.replaceChaine(pojoUssd.getMessage()));
                sessionussd.setAccess("CNI");
                ussdRepository.save(sessionussd);
                map.put("message", "Entr"
                        + "entrez le Numero de CNI~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save CNI
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("CNI") && sessionussd.getType().equals("-1")) {

                response = new Responses();
                response = ussdservice.CheckCni(pojoUssd.getMessage());

                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("Sexe");
                sessionussd.setCni(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Choisir le Sexe~1.MAsculin~2.Feminin~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save Sexe
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("Sexe") && sessionussd.getType().equals("-1")) {

                response = new Responses();
                response = ussdservice.CheckSexe(pojoUssd.getMessage());

                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg() + "~1.MAsculin~2.Feminin~0.Annuler");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg() + "~1.MAsculin~2.Feminin~0.Annuler");
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("Naissance");
                sessionussd.setSexe(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez la Date de Naissance au format JJ/MM/AAAA~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save Date
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("Naissance") && sessionussd.getType().equals("-1")) {

                response = new Responses();
                response = ussdservice.CheckDate(pojoUssd.getMessage());

                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("Lieu");
                sessionussd.setDatenaissance(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le Lieu de Naissance~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save lieu lieu de naissance
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("Lieu") && sessionussd.getType().equals("-1")) {

                sessionussd.setAccess("Contribuable");
                sessionussd.setLieunaissance(ussdservice.replaceChaine(pojoUssd.getMessage()));
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le Numero de Contribuable~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save Contribuable 
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("Contribuable") && sessionussd.getType().equals("-1")) {
                response = new Responses();
                response = ussdservice.CheckContribuable(pojoUssd.getMessage());

                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("email");
                sessionussd.setNumbcontribuable(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez votre adresse email~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save Email       
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("email") && sessionussd.getType().equals("-1")) {
                System.out.println(pojoUssd.getMessage());
                System.out.println(ussdservice.replaceChaine2(pojoUssd.getMessage()));
                response = new Responses();
                response = ussdservice.CheckEmail(ussdservice.replaceChaine2(pojoUssd.getMessage()));

                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setAccess("secret");
                sessionussd.setEmail(ussdservice.replaceChaine2(pojoUssd.getMessage()));
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez votre code secret~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("secret") && sessionussd.getType().equals("-1")) {

                sessionussd.setAccess("confirmeCode");
                sessionussd.setCodesecret(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Confirmez votre CodeSecret~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Save confirmation du code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getAccess().equals("confirmeCode") && sessionussd.getType().equals("-1")) {
                response = new Responses();
                response = ussdservice.ConfirmCodeSecret(sessionussd.getCodesecret(), pojoUssd.getMessage());
                System.out.println(response.getSucces());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                System.out.println(response.getSucces());
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                Responses response1 = new Responses();
                response1 = ussdservice.CreateAccountUssd(sessionussd);
                System.out.println(ussdservice.CreateAccountUssd(sessionussd));
                sessionussd.setAccess(null);
                sessionussd.setNewcode(null);
                sessionussd.setCodesecret(null);
                sessionussd.setNewcode(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", response1.getMsg());
                map.put("command", "0");
                return map;
            }

            //Transfert d'argent ---------------------------------------------------     
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
                if (response.getSucces() == -1) {
                    sessionussd.setAccess("1");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    sessionussd.setAccess("0");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                sessionussd.setLastsep(sessionussd.getMessage() + "*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si c'est un transfert d'un compte perfectpay à un compte perfectpay------------------------------------------------------
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*1*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrez le numero de telephone du beneficiaire~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (sessionussd.getLastsep().equals("237*100*1*1") && !"0".equals(pojoUssd.getMessage()) && sessionussd.getDestinataire() != null && sessionussd.getMontant() != null && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.validationTransfert(sessionussd.getMontant().toString(), pojoUssd.getMsisdn(), sessionussd.getDestinataire(), pojoUssd.getMessage());
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Retour ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Retour ");
                    map.put("command", "0");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                if (response.getSucces() == -10) {

                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -2) {

                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {

                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -5) {

                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                System.out.println(sessionussd.getMontant());
                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;

            }

            if (sessionussd.getLastsep().equals("237*100*1*1") && !"0".equals(pojoUssd.getMessage()) && sessionussd.getDestinataire() != null && sessionussd.getMontant() == null && sessionussd.getType().equals("2")) {

                response = new Responses();
                response = ussdservice.CheckSoldeTransfert(pojoUssd.getMessage(), pojoUssd.getMsisdn(), sessionussd.getDestinataire());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg() + "~0.Retour ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg() + "~0.Retour ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Retour ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
            }

            //retour 
            if (sessionussd.getLastsep().equals("237*100*1*1") && pojoUssd.getMessage().equals("0") && sessionussd.getDestinataire() != null && sessionussd.getMontant() == null && sessionussd.getType().equals("2")) {
                sessionussd.setDestinataire(null);
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le numero de telephone du beneficiaire~0.Retour ");
                map.put("command", "1");
                return map;

            }
            //retour
            if (sessionussd.getLastsep().equals("237*100*1*1") && pojoUssd.getMessage().equals("0") && sessionussd.getDestinataire() != null && sessionussd.getMontant() != null && sessionussd.getType().equals("2")) {
                sessionussd.setMontant(null);
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le montant~0.Retour ");
                map.put("command", "1");
                return map;

            }

            //entrer le montant pour le transfert
            if (sessionussd.getLastsep().equals("237*100*1*1") && !"0".equals(pojoUssd.getMessage()) && sessionussd.getMontant() == null && sessionussd.getType().equals("2")) {
                response = new Responses();

                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());

                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println("destinataire");
                response = ussdservice.CheckCompteDestinaire(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg() + "~0.Retour ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg() + "~0.Retour ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Retour ");
                    map.put("command", "1");
                    return map;
                }
                System.out.println(response.getSucces());
                if (response.getSucces() == 1) {
                    sessionussd.setDestinataire(pojoUssd.getMessage());
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrez le montant~0.Retour ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 19) {
                    sessionussd.setDestinataire(pojoUssd.getMessage());
                    ussdRepository.save(sessionussd);
                    map.put("message", "" + response.getMsg());
                    map.put("command", "1");
                    return map;
                }
            }

            //Si c'est un transfert d'un compte perfectpay à un compte Mtn Money*******************************************************         
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1*2");
                ussdRepository.save(sessionussd);
                map.put("message", "Service momentanement indisponible~0.Retour ");
                map.put("command", "0");
                return map;
            }

            //Si c'est un transfert d'un compte perfectpay à un compte Orange Money*********************************************************        
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1*3");
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le numero de telephone du destinataire ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*3") && sessionussd.getDestinataire() == null && sessionussd.getType().equals("2")) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*3") && sessionussd.getMontant() == null && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.checker_solde_transfert_autre_compte_OrangeMoney_USSD(sessionussd.getMsisdn().substring(3), sessionussd.getDestinataire(), pojoUssd.getMessage());
                if (response.getSucces() == -2) {
                    map.put("message", "" + response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la raison du transfert ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*3") && sessionussd.getRaisontransfert() == null && sessionussd.getType().equals("2")) {
                if (pojoUssd.getMessage() != null) {
                    sessionussd.setRaisontransfert(pojoUssd.getMessage());
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer votre code secret ");
                    map.put("command", "1");
                    return map;
                }
                if (pojoUssd.getMessage() == null) {
                    sessionussd.setRaisontransfert("Transfert perfectpay vers Orange Money");
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer votre code secret ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*3") && sessionussd.getCodesecret() == null && sessionussd.getType().equals("2")) {
                response = new Responses();
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.transfert_account_perfect_pay_vers_orangeMoney_USSD(sessionussd);
                System.out.println(response.getSucces() + "-*-*-*--*-*-*----------------------------***********************");
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("Ok");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -4) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -5) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -7) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -8) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -9) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -10) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
            }

            //Si c'est un transfert d'un compte perfectpay à un compte EU Mobil Money        
            if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1*4");
                ussdRepository.save(sessionussd);
                //Transfert d'argent Vers un client EU Mobil Money~Entrer le numero du destinataire~0.Retour//        
                map.put("message", "Service momentanement indisponible~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si c'est un transfert d'un compte perfectpay à un compte Yup       
            if (pojoUssd.getMessage().equals("5") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1*5");
                ussdRepository.save(sessionussd);
                //Transfert d'argent Vers un client Yup~Entrer le numero du destinataire~0.Retour//
                map.put("message", "Service momentanement indisponible~0.Retour ");
                map.put("command", "0");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu transfert      
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*1") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu transfert      
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*2") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu transfert      
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*3") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu transfert      
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*4") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu transfert      
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*5") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //pour retourner au menu principal étant sur le menu transfert d'argent
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);

                map.put("message", "Bienvenue sur PerfectPay Client~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~6.Retrait PerfectPay~7.GimacPay~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //paiemennt marchand  ------------------------------------------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("2")) {

                response = new Responses();

                response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
                if (response.getSucces() == -1) {
                    sessionussd.setAccess("1");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    sessionussd.setAccess("0");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                sessionussd.setLastsep(sessionussd.getMessage() + "*2");
                ussdRepository.save(sessionussd);
                map.put("message", "Paiement Marchand~1.Nouveau Paiement~2.Checker un code marchand~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Pour un nouveau paiement Marchand ----------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("2") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*2*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer le entrer le code marchand~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Checker un code marchand-------------------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("2") && sessionussd.getType().equals("2")) {

                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*2*2");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer le entrer le code marchand~0.Retour ");
                map.put("command", "1");
                return map;
            }

            // Pour Checker le solde pour effectuer un paiement marchand
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*1") && sessionussd.getCodemarchant() != null && sessionussd.getMontant() == null && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.checkSoldePaiment(sessionussd.getCodemarchant(), pojoUssd.getMsisdn(), Double.parseDouble(pojoUssd.getMessage()));
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                ussdRepository.save(sessionussd);

                map.put("message", response.getMsg() + "Entrez le entrer le code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            // Pour effectuer le paiement marchant en checkant le code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*1") && sessionussd.getCodemarchant() != null && sessionussd.getMontant() != null && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.MakePaimentMarchand(sessionussd.getCodemarchant(), pojoUssd.getMsisdn(), sessionussd.getMontant(), pojoUssd.getMessage());
                if (response.getSucces() == -4) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

                sessionussd.setCodesecret(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);

                map.put("message", response.getMsg() + "~0.Retour ");
                map.put("command", "0");
                return map;
            }

            //pour checker le code secret pour consulter sont code secret-----------------------------------------------------
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*2") && sessionussd.getCodemarchant() != null && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.checkerCodeSecret2(sessionussd.getCodemarchant(), pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //pour checker le code marchant pour faire un paiement marchand
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*1") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.checkerCodeMarchant(pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setCodemarchant(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);

                map.put("message", "Veuillez Entrer le montant~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //pour checker le code marchant pour checker
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*2") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.checkerCodeMarchant(pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setCodemarchant(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);

                map.put("message", " Entrer le code secret ~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu paiement marchand
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*2");
                sessionussd.setCodemarchant(null);
                sessionussd.setMontant(null);
                sessionussd.setCodesecret(null);
                ussdRepository.save(sessionussd);

                map.put("message", "Paiement Marchand~1.Nouveau Paiement~2.Checker un code marchand~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu paiement marchand
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*2");
                sessionussd.setCodemarchant(null);
                sessionussd.setMontant(null);
                sessionussd.setCodesecret(null);
                ussdRepository.save(sessionussd);

                map.put("message", "Paiement Marchand~1.Nouveau Paiement~2.Checker un code marchand~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //pour retourner au menu principal étant sur le menu paiement marchant
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);

                map.put("message", "Bienvenue sur PerfectPay Client~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~6.Retrait PerfectPay~7.GimacPay~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //opération banquaire  ------------------------------------------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
                if (response.getSucces() == -1) {
                    sessionussd.setAccess("1");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    sessionussd.setAccess("0");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                sessionussd.setLastsep(sessionussd.getMessage() + "*3");
                ussdRepository.save(sessionussd);
                map.put("message", "Selectionner votre banque parmi nos banques partenaires~1.UBA~2.Afriland~3.Banque Atlantique~4.Ecobank~5.Societe Generale~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Pour un nouveau paiement 
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("2")) {

                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*3*1");
                ussdRepository.save(sessionussd);

                map.put("message", "UBA BANK~1.Transfert PerfectPay vers Carte Bancaire~2.Transfert PerfectPay vers Compte Bancaire~3.Transfert Carte bancaire vers Carte bancaire~4.Transfert Carte bancaire vers Compte bancaire~5.Transfert Compte bancaire vers Carte bancaire~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu opération bancaire
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*3*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*3");
                ussdRepository.save(sessionussd);

                map.put("message", "Selectionner votre banque parmi nos banques partenaires~1.UBA~2.Afriland~3.Banque Atlantique~4.Ecobank~5.Societe Generale~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //pour retourner au menu principal étant sur le menu opération bancaire
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*3") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);

                map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Service tierce  ------------------------------------------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
                if (response.getSucces() == -1) {
                    sessionussd.setAccess("1");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    sessionussd.setAccess("0");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                sessionussd.setLastsep(sessionussd.getMessage() + "*4");
                ussdRepository.save(sessionussd);
                map.put("message", "Services Tiers~1.Payez un abonnement GEDOMED~2.Payez une licence IPLANS ERP~3.Payez Cotisation ONMC~4.Payez Frais etude de dossier ONMC~5.Payez Frais de siege ONMC~6.Payez Facture ENEO~7.Payez Facture CAMWATER~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //pour retourner au menu principal étant sur le menu service tierce
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*4") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);

                map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Mon compte PerfectPay  ------------------------------------------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("5") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("2")) {

                response = new Responses();
                response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
                if (response.getSucces() == -1) {
                    sessionussd.setAccess("1");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    sessionussd.setAccess("0");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                sessionussd.setLastsep(sessionussd.getMessage() + "*5");
                ussdRepository.save(sessionussd);
                map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Consulter solde
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*5") && sessionussd.getType().equals("2")) {

                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*5*1");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //checker le code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*1") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

                response = ussdservice.CheckCodeSecret(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*1") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*5");
                ussdRepository.save(sessionussd);

                map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Modifier Code PIN ----------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*5") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*5*2");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre ancien code ping~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //etape  pour verifier le nouveau code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*2") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() == null && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.CheckNouveauCode(sessionussd.getCodesecret(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setNewcode(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Confirmer le nouveau code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }
            //etape  pour confirmer le code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*2") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() != null && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.updateCodeSecret(sessionussd.getCodesecret(), pojoUssd.getMsisdn(), pojoUssd.getMessage(), sessionussd.getNewcode());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == -5) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //Checker l'ancient code secret Ussd
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*2") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.CheckAncienCode(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                sessionussd.setCodesecret(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le nouveau code secret~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Consulter dernières transactions------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*5") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }
                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*5*3");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer votre code secret~0.Retour ");
                map.put("command", "1");

                return map;
            }

            //checker le code ping pour afficher la liste des transactions
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*3") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.CheckListTransation(pojoUssd.getMsisdn(), pojoUssd.getMessage());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 0) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                map.put("message", response.getMsg());
                map.put("command", "0");
                return map;
            }

            //Retrait PerfectPay-------------------------------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("6") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("2")) {
                response = new Responses();
                response = ussdservice.Check_Etat_Compte_USSD(pojoUssd.getMsisdn());
                if (response.getSucces() == 2) {
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

                sessionussd.setLastsep("237*100*6");
                ussdRepository.save(sessionussd);

                map.put("message", "Entrer le code du point de vente ");
                map.put("command", "1");
                return map;
            }

            //Etape Checker code du point de vente
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*6") && sessionussd.getType().equals("2") && sessionussd.getCodemarchant() == null) {
                response = new Responses();
                response = ussdservice.checker_CodePointVente(pojoUssd.getMessage());
                System.out.println(response.getSucces());
                if (response.getSucces() == -1) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodemarchant(pojoUssd.getMessage());
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer le montant ");
                    map.put("command", "1");
                    return map;
                }

            }

            //Etape checker montant du client
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*6") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                response = ussdservice.checker_solde_Client_New_retrait(pojoUssd.getMessage(), sessionussd.getCodemarchant(), pojoUssd.getMsisdn());

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.parseDouble(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

            }

            //Etape Checker le code Secret du client pour finaliser le retrait d'argent au point de vente
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*6") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                response = new Responses();
                response = ussdservice.validation_retrait_account_perfect_pay_NewMethode(sessionussd.getMontant().toString(), sessionussd.getCodemarchant(), pojoUssd.getMsisdn(), pojoUssd.getMessage());
                System.out.println(response.getSucces() + " " + response.getSucces());
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret(pojoUssd.getMessage());
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "1");
                    return map;
                }

            }

            //Si GimacPAY est selectionne par l'utilisateur--------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("7") && sessionussd.getLastsep().equals("237*100") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7");
                ussdRepository.save(sessionussd);
                map.put("message", "GimacPay~1.Transfert wallet~2.Paiement marchand~3.Paiement de facture~4.Retrait sans carte~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Transfert wallet******************************************************************************************************
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert wallet~1.Transfert MNO~2.Transfert bancaire~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Transfert Transfert MNO-..................................................
            //etape Selection du pays
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert MNO/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~4.Republique Centrafricaine~5.Tchad~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Affiche liste operateur en fonction du pays**//**********************************
            //Si 1.Cameroun ----------------------CAMEROUN----------------------------------------------------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*1");
                sessionussd.setPays("1");
                ussdRepository.save(sessionussd);
                map.put("message", "Cameroun/Selectioner la Wallet recipiendaire~1.Orange Money~2.MTN MoMo~3.YUP de SG~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Operation Wallet ORANGE MONEY.........................................................................
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7*1*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*1*1");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "Orange Money");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*1") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*1") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*1") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*1") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }

            //Operation Wallet MTN MoMo........................................................................................
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*7*1*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*1*2");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "MTN MoMo");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*2") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*2") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*2") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*2") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }

            //Operation Wallet YUP de SG........................................................................................
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*7*1*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*1*3");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "YUP de SG");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*3") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*3") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*3") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1*3") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }

            //Si 2.Congo----------------------CONGO---------------------------------------------------------------------------------------------------------------------------------------------- 
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*7*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*2");
                sessionussd.setPays("2");
                ussdRepository.save(sessionussd);
                map.put("message", "Congo/Selectioner la wallet~1.Airtel Money~2.MTN MoMo~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Operation Wallet Airtel Money Congo.........................................................................
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7*1*1*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*2*1");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "Airtel Money");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2*1") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2*1") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2*1") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2*1") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }

            //Operation Wallet MTN MoMo Congo........................................................................................
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*7*1*1*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*2*2");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "MTN MoMo");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2*2") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2*2") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2*2") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2*2") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }

            //Si 3.Gabon----------------------GABON----------------------------------------------------------------------------------------------------------------------------------------------  
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*7*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*3");
                sessionussd.setPays("3");
                ussdRepository.save(sessionussd);
                map.put("message", "Gabon/Selectioner la wallet~1.Airtel Money~2.Moov Money~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Operation Wallet Airtel Money GABON.........................................................................
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7*1*1*3") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*3*1");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "Airtel Money");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3*1") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3*1") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3*1") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3*1") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }

            //Operation Wallet Moov Money GABON........................................................................................
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*7*1*1*3") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*3*2");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "Moov Money");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3*2") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3*2") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3*2") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3*2") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }
            

            //Si 4.Republique Centrafricaine----------------------REPUBLIQUE CENTREAFRICAINE----------------------------------------------------------------------------------------------------------------------------------------------  
            if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100*7*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*4");
                sessionussd.setPays("4");
                ussdRepository.save(sessionussd);
                map.put("message", "Republique Centrafricaine/Selectioner la wallet~1.Orange Money~0.Retour ");
                map.put("command", "1");
                return map;
            }
            
             //Operation Wallet Orange Money Republique Centrafricaine........................................................................................
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7*1*1*4") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*4*1");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "Orange Money");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*4*1") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*4*1") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*4*1") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*4*1") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }

            //Si 5.Tchad-----------------------------TCHAD-----------------------------------------------------------------------------------------------------
            if (pojoUssd.getMessage().equals("5") && sessionussd.getLastsep().equals("237*100*7*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*5");
                sessionussd.setPays("5");
                ussdRepository.save(sessionussd);
                map.put("message", "Tchad/Selectioner la wallet~1.Airtel Money~2.Moov Money~0.Retour ");
                map.put("command", "1");
                return map;
            }
            
              //Operation Wallet Airtel Money tchad........................................................................................
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7*1*1*5") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*5*1");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "Airtel Money");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5*1") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5*1") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5*1") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5*1") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }
            
             //Operation Wallet Moov Money tchad........................................................................................
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*7*1*1*5") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1*5*2");
                Waletmobilegimac waletmobilegimac = waletmobilRepository.findByWaletmobilegimac(Integer.parseInt(sessionussd.getPays()), "Airtel Money");
                sessionussd.setWallet(waletmobilegimac.getCodeWalet());
                System.out.println(waletmobilegimac.getCodeWalet());
                ussdRepository.save(sessionussd);
                map.put("message", "Saisir le numero de telephone recipiendaire~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5*2") && sessionussd.getType().equals("2") && sessionussd.getDestinataire() == null) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le montant a transferer~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5*2") && sessionussd.getType().equals("2") && sessionussd.getMontant() == null) {
                response = new Responses();
                try {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                } catch (Exception e) {
                    map.put("message", "Le montant doit contenir uniquement les chiffres~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                response = ussdservice.Solde_transfert_walet_MNO_USSD(sessionussd);
                System.out.println(response.getSucces() + " " + response.getMsg());
                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -3) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 18) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }

                if (response.getSucces() == 1) {
                    sessionussd.setMontant(Double.valueOf(pojoUssd.getMessage()));
                    ussdRepository.save(sessionussd);
                    map.put("message", "Entrer la reference pour identifier la transaction~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5*2") && sessionussd.getType().equals("2") && sessionussd.getReference() == null) {
                sessionussd.setReference(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrer le mot de passe pour confirmer la transaction~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            if (!pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5*2") && sessionussd.getType().equals("2") && sessionussd.getCodesecret() == null) {
                sessionussd.setCodesecret(pojoUssd.getMessage());
                response = ussdservice.Valide_Transfert_walet_MNO_USSD(sessionussd);

                if (response.getSucces() == -2) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -6) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == -22) {
                    map.put("message", response.getMsg() + "~0.Annuler ");
                    map.put("command", "1");
                    return map;
                }
                if (response.getSucces() == 1) {
                    sessionussd.setCodesecret("OK");
                    ussdRepository.save(sessionussd);
                    map.put("message", response.getMsg());
                    map.put("command", "0");
                    return map;
                }

            }
            

            //Transfert banque-..........................TRANSFERT BANQUE..........................................................................
            //etape Selection du pays
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*7*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*2");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert Bancaire/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Affiche liste bancaire en fonction du pays**//
            //Si 1.Cameroun 
            if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*7*1*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*2*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Selectioner la banque~1.BICEC~2.CBC~3.CCA Bank~4.Express Union~5.La Régionale~6.UBC~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si 2.Congo 
            if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*7*1*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*2*2");
                ussdRepository.save(sessionussd);
                map.put("message", "Selectioner la banque~1.BCI Mobile~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si 3.Gabon 
            if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*7*1*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*2*3");
                ussdRepository.save(sessionussd);
                map.put("message", "Selectioner la banque~1.BGFI Gabon~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //retour au menu Gimac***********************************************************------------------------//
            ////retour au menu selecttion pays a partir de l'option wallet selectionner
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert MNO/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~4.Republique Centrafricaine~5.Tchad~0.Retour ");
                map.put("command", "1");
                return map;
            }
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert MNO/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~4.Republique Centrafricaine~5.Tchad~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*3") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert MNO/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~4.Republique Centrafricaine~5.Tchad~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*4") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert MNO/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~4.Republique Centrafricaine~5.Tchad~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1*5") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert MNO/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~4.Republique Centrafricaine~5.Tchad~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //retour au menu selecttion pays a partir de  l'option Banque selectionner
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*2*3") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*2");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert Bancaire/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*2*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*2");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert Bancaire/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~0.Retour ");
                map.put("command", "1");
                return map;
            }
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*2*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1*2");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert Bancaire/Selectioner le pays~1.Cameroun~2.Congo~3.Gabon~0.Retour ");
                map.put("command", "1");
                return map;
            }
            //retour au menu transfer wallet
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert wallet~1.Transfert MNO~2.Transfert banque~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7*1");
                ussdRepository.save(sessionussd);
                map.put("message", "Transfert wallet~1.Transfert MNO~2.Transfert banque~0.Retour ");
                map.put("command", "1");
                return map;
            }
            //retour au menu GimacPay
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*7");
                ussdRepository.save(sessionussd);
                map.put("message", "GimacPay~1.Transfert wallet~2.Paiement marchand~3.Paiement de facture~4.Retrait sans carte~0.Retour ");
                map.put("command", "1");
                return map;
            }
            //retour au menu PerfectPay Client
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*7") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);
                map.put("message", "Bienvenue sur PerfectPay Client~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon Compte~6.Retrait Perfectpay~7.GimacPay~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //retour au sous menu mon compte PerfectPay
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*1") && sessionussd.getType().equals("2")) {

                sessionussd.setLastsep("237*100*5");
                ussdRepository.save(sessionussd);
                map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Changer de langue
            if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100*5") && sessionussd.getType().equals("2")) {

                System.out.println(sessionussd.getLastsep());
                sessionussd.setLastsep("237*100*5*4");
                ussdRepository.save(sessionussd);
                //  Choix de la langue~1.Français~2.Englais~0.Retour//
                map.put("message", "Service momentanement indisponible~0.Retour ");
                map.put("command", "0");
                return map;
            }
            //pour retourner au menu principal étant sur le menu service tierce
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);

                map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu mon compte Perfectpay
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*1") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*5");
                ussdRepository.save(sessionussd);

                map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
                map.put("command", "1");
                return map;
            }

            //Si le user entre 0 pour rentrer au menu mon compte Perfectpay
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*2") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*5");
                ussdRepository.save(sessionussd);

                map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
                map.put("command", "1");
                return map;
            }
            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*3") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*5");
                ussdRepository.save(sessionussd);

                map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*4") && sessionussd.getType().equals("2")) {
                sessionussd.setLastsep("237*100*5");
                ussdRepository.save(sessionussd);

                map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
                map.put("command", "1");
                return map;
            }
            if (pojoUssd.getMessage()
                    .equals("0")) {
                map.put("message", "Operation annulee!");
                map.put("command", "0");
                return map;
            } else {
                System.out.println("Service momentanement insdisponible");
                map.put("message", "Service momentanement insdisponible~0.Retour ");
                map.put("command", "0");

            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            map.put("message", "Connexion Error ");
            map.put("command", "0");
            return map;
        }

        return map;
    }

}
