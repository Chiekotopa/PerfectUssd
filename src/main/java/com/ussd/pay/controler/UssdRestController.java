/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.controler;

import com.ussd.pay.dao.SessionUssdRepository;
import com.ussd.pay.entities.Sessionussd;
import com.ussd.pay.pojo.PojoUssd;
import com.ussd.pay.pojo.Responses;
import com.ussd.pay.service.UssdService;
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

    @RequestMapping(value = "/ussd", method = RequestMethod.POST)
    public HashMap getUssd(@RequestBody PojoUssd pojoUssd) {
        HashMap map = new HashMap();
        Sessionussd sessionussd = new Sessionussd();
        if (ussdRepository.findApiBySessionId(pojoUssd.getSessionid()) == null) {

            if (pojoUssd.getMessage().equals("237*100")) {

                sessionussd = new Sessionussd();
                sessionussd.setMessage(pojoUssd.getMessage());
                sessionussd.setMsisdn(pojoUssd.getMsisdn());
                sessionussd.setProvider(pojoUssd.getProvider());
                sessionussd.setSessionid(pojoUssd.getSessionid());
                sessionussd.setLastsep("237*100");
                ussdRepository.save(sessionussd);
                map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
                map.put("command", "1");
                return map;
            }
        }       
        sessionussd = new Sessionussd();
        sessionussd = ussdRepository.findApiBySessionId(pojoUssd.getSessionid());
       
        
        if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100")) {
            Responses response = new Responses();
            response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
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
            sessionussd.setAccess(1);
            sessionussd.setLastsep(sessionussd.getMessage() + "*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si c'est un transfert d'un compte perfectpay à un compte perfectpay
        if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*1")) {

            System.out.println(sessionussd.getLastsep());
            sessionussd.setLastsep("237*100*1*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Entrez le numero de telephone du beneficiaire~0.Retour ");
            map.put("command", "1");
            return map;
        }

        if (sessionussd.getLastsep().equals("237*100*1*1") && !"0".equals(pojoUssd.getMessage()) && sessionussd.getDestinataire() != null && sessionussd.getMontant() != null && sessionussd.getCodesecret() == null) {
            Responses response = new Responses();
            response = ussdservice.validationTransfert(sessionussd.getMontant().toString(), pojoUssd.getMsisdn(), sessionussd.getDestinataire(), pojoUssd.getMessage());
            if (response.getSucces() == -6) {
                map.put("message", response.getMsg() + "~0.Retour ");
                map.put("command", "1");
                return map;
            }

            if (response.getSucces() == 1) {
                sessionussd.setCodesecret("OK");
                ussdRepository.save(sessionussd);
                map.put("message", response.getMsg());
                map.put("command", "1");
                return map;
            }
        }

        if (sessionussd.getLastsep().equals("237*100*1*1") && !"0".equals(pojoUssd.getMessage()) && sessionussd.getDestinataire() != null) {
            Responses response = new Responses();
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

        if (sessionussd.getLastsep().equals("237*100*1*1") && pojoUssd.getMessage().equals("0") && sessionussd.getDestinataire() != null && sessionussd.getMontant() == null) {
            sessionussd.setDestinataire(null);
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez le numero de telephone du beneficiaire~0.Retour ");
            map.put("command", "1");
            return map;

        }

        if (sessionussd.getLastsep().equals("237*100*1*1") && pojoUssd.getMessage().equals("0") && sessionussd.getDestinataire() != null && sessionussd.getMontant() != null) {
            sessionussd.setMontant(null);
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez le montant~0.Retour ");
            map.put("command", "1");
            return map;

        }

        if (sessionussd.getLastsep().equals("237*100*1*1") && !"0".equals(pojoUssd.getMessage())) {
            Responses response = new Responses();
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
            if (response.getSucces() == 1) {
                sessionussd.setDestinataire(pojoUssd.getMessage());
                ussdRepository.save(sessionussd);
                map.put("message", "Entrez le montant~0.Retour ");
                map.put("command", "1");
                return map;
            }
        }

        //Si c'est un transfert d'un compte perfectpay à un compte Mtn Money         
        if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*1")) {

            sessionussd.setLastsep("237*100*1*2");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent Vers un client Mtn Money~Entrer le numero du destinataire~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si c'est un transfert d'un compte perfectpay à un compte Orange Money         
        if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*1")) {

            sessionussd.setLastsep("237*100*1*3");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent Vers un client Orange Money~Entrer le numero du destinataire~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si c'est un transfert d'un compte perfectpay à un compte EU Mobil Money        
        if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100*1")) {

            sessionussd.setLastsep("237*100*1*4");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent Vers un client EU Mobil Money~Entrer le numero du destinataire~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si c'est un transfert d'un compte perfectpay à un compte Yup       
        if (pojoUssd.getMessage().equals("5") && sessionussd.getLastsep().equals("237*100*1")) {

            sessionussd.setLastsep("237*100*1*5");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent Vers un client Yup~Entrer le numero du destinataire~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu transfert      
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*1")) {

            sessionussd.setLastsep("237*100*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu transfert      
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*2")) {

            sessionussd.setLastsep("237*100*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu transfert      
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*3")) {

            sessionussd.setLastsep("237*100*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu transfert      
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*4")) {

            sessionussd.setLastsep("237*100*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu transfert      
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1*5")) {

            sessionussd.setLastsep("237*100*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~0.Retour  ");
            map.put("command", "1");
            return map;
        }

        //pour retourner au menu principal étant sur le menu transfert d'argent
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*1")) {
            sessionussd.setLastsep("237*100");
            ussdRepository.save(sessionussd);

            map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
            map.put("command", "1");
            return map;
        }
        
        
        
        
        
    
        //paiemennt marchand  ------------------------------------------------------------------------------------------------------------------------------------------
        if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100")) {

            Responses response = new Responses();
            response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
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
            sessionussd.setAccess(1);
            sessionussd.setLastsep(sessionussd.getMessage() + "*2");
            ussdRepository.save(sessionussd);
            map.put("message", "Paiement Marchand~1.Nouveau Paiement~2.Checker un code marchand~0.Retour");
            map.put("command", "1");
            return map;
        }

        //Pour un nouveau paiement 
        if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*2")) {

            System.out.println(sessionussd.getLastsep());
            sessionussd.setLastsep("237*100*2*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Entrer le entrer le code marchand~0.Retour ");
            map.put("command", "1");
            return map;
        }
          if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*2")) {

            System.out.println(sessionussd.getLastsep());
            sessionussd.setLastsep("237*100*2*2");
            ussdRepository.save(sessionussd);

            map.put("message", "Entrer le entrer le code marchand~0.Retour ");
            map.put("command", "1");
            return map;
        }
          
          
           // Pour Checker le solde pour effectuer un paiement marchand
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*1")&& sessionussd.getCodemarchant()!=null && sessionussd.getMontant()==null) {
            Responses response = new Responses();
            response=ussdservice.checkSoldePaiment(sessionussd.getCodemarchant(),pojoUssd.getMsisdn(),Double.parseDouble(pojoUssd.getMessage()));
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

            map.put("message", response.getMsg()+"Entrez le entrer le code secret~0.Retour ");
            map.put("command", "1");
            return map;
        }
            // Pour effectuer le paiement en checkant le code secret
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*1")&& sessionussd.getCodemarchant()!=null&& sessionussd.getMontant()!=null) {
            Responses response = new Responses();
            response=ussdservice.MakePaimentMarchand(sessionussd.getCodemarchant(),pojoUssd.getMsisdn(),sessionussd.getMontant(),pojoUssd.getMessage());
             if (response.getSucces() == -4) {
                map.put("message", response.getMsg());
                map.put("command", "1");
                return map;
            }
           
            sessionussd.setCodesecret(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);

            map.put("message", response.getMsg()+"~0.Retour ");
            map.put("command", "1");
            return map;
        }
        
        
          //pour checker le code secret2
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*2")&& sessionussd.getCodemarchant()!=null) {
            Responses response = new Responses();
            response = ussdservice.checkerCodeSecret2(sessionussd.getCodemarchant(),pojoUssd.getMsisdn(),pojoUssd.getMessage());
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
                     
            map.put("message", response.getMsg()+"~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //pour checker le code marchant pour faire un paiement marchand
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*1")) {
            Responses response = new Responses();
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
         if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*2*2")) {
            Responses response = new Responses();
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
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2*1")) {
            sessionussd.setLastsep("237*100*2");
            sessionussd.setCodemarchant(null);
            sessionussd.setMontant(null);
            sessionussd.setCodesecret(null);
            ussdRepository.save(sessionussd);

            map.put("message", "Paiement Marchand~1.Nouveau Paiement~2.Checker un code marchand~0.Retour");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu paiement marchand
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2*2")) {
            sessionussd.setLastsep("237*100*2");
            sessionussd.setCodemarchant(null);
            sessionussd.setMontant(null);
            sessionussd.setCodesecret(null);
            ussdRepository.save(sessionussd);

            map.put("message", "Paiement Marchand~1.Nouveau Paiement~2.Checker un code marchand~0.Retour");
            map.put("command", "1");
            return map;
        }

        //pour retourner au menu principal étant sur le menu paiement marchant
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*2")) {
            sessionussd.setLastsep("237*100");
            ussdRepository.save(sessionussd);

            map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
            map.put("command", "1");
            return map;
        }

        //opération banquaire  ------------------------------------------------------------------------------------------------------------------------------------------
        if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100")) {
            Responses response = new Responses();
            response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
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
            sessionussd.setAccess(1);
            sessionussd.setLastsep(sessionussd.getMessage() + "*3");
            ussdRepository.save(sessionussd);
            map.put("message", "Selectionner votre banque parmi nos banques partenaires~1.UBA~2.Afriland~3.Banque Atlantique~4.Ecobank~5.Societe Generale~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Pour un nouveau paiement 
        if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*3")) {

            System.out.println(sessionussd.getLastsep());
            sessionussd.setLastsep("237*100*3*1");
            ussdRepository.save(sessionussd);

            map.put("message", "UBA BANK~1.Transfert PerfectPay vers Carte Bancaire~2.Transfert PerfectPay vers Compte Bancaire~3.Transfert Carte bancaire vers Carte bancaire~4.Transfert Carte bancaire vers Compte bancaire~5.Transfert Compte bancaire vers Carte bancaire~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu opération bancaire
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*3*1")) {
            sessionussd.setLastsep("237*100*3");
            ussdRepository.save(sessionussd);

            map.put("message", "Selectionner votre banque parmi nos banques partenaires~1.UBA~2.Afriland~3.Banque Atlantique~4.Ecobank~5.Societe Generale~0.Retour");
            map.put("command", "1");
            return map;
        }

        //pour retourner au menu principal étant sur le menu opération bancaire
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*3")) {
            sessionussd.setLastsep("237*100");
            ussdRepository.save(sessionussd);

            map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
            map.put("command", "1");
            return map;
        }

        //Service tierce  ------------------------------------------------------------------------------------------------------------------------------------------
        if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100")) {
            Responses response = new Responses();
            response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
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
            sessionussd.setAccess(1);
            sessionussd.setLastsep(sessionussd.getMessage() + "*4");
            ussdRepository.save(sessionussd);
            map.put("message", "Services Tierces~1.Payez un abonnement GEDOMED~2.Payez une licence IPLANS ERP~3.Payer Cotisation ONMC~4.Payer Frais etude de dossier ONMC~5.Payer Frais de siege ONMC~6.Payer Facture ENEO~7.Payer Facture CAMWATER~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //pour retourner au menu principal étant sur le menu service tierce
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*4")) {
            sessionussd.setLastsep("237*100");
            ussdRepository.save(sessionussd);

            map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
            map.put("command", "1");
            return map;
        }

   
        
        
        
       
        //Mon compte PerfectPay  ------------------------------------------------------------------------------------------------------------------------------------------
        if (pojoUssd.getMessage().equals("5") && sessionussd.getLastsep().equals("237*100")) {

            Responses response = new Responses();
            response = ussdservice.CheckCompteExpediteur(pojoUssd.getMsisdn());
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
            sessionussd.setAccess(1);
            sessionussd.setLastsep(sessionussd.getMessage() + "*5");
            ussdRepository.save(sessionussd);
            map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Consulter solde
        if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100*5")) {

            System.out.println(sessionussd.getLastsep());
            sessionussd.setLastsep("237*100*5*1");
            ussdRepository.save(sessionussd);

            map.put("message", "Entrer votre code secret~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //checker le code secret
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*1")) {
            Responses response = new Responses();
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

            map.put("message", response.getMsg() + "~0.Retour ");
            map.put("command", "1");
            return map;
        }

        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*1")) {

            sessionussd.setLastsep("237*100*5");
            ussdRepository.save(sessionussd);

            map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Modifier Code PIN
        if (pojoUssd.getMessage().equals("2") && sessionussd.getLastsep().equals("237*100*5")) {

            System.out.println(sessionussd.getLastsep());
            sessionussd.setLastsep("237*100*5*2");
            ussdRepository.save(sessionussd);

            map.put("message", "Entrer votre ancien code ping~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //etape  pour verifier le nouveau code secret
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*2") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() == null) {
            Responses response = new Responses();
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
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*2") && sessionussd.getCodesecret() != null && sessionussd.getNewcode() != null) {
            Responses response = new Responses();
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
            map.put("command", "1");
            return map;
        }

        //Checker l'ancient code secret Ussd
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*2")) {
            Responses response = new Responses();
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
            return map;
        }

        //Consulter dernières transactions
        if (pojoUssd.getMessage().equals("3") && sessionussd.getLastsep().equals("237*100*5")) {

            System.out.println(sessionussd.getLastsep());
            sessionussd.setLastsep("237*100*5*3");
            ussdRepository.save(sessionussd);

            map.put("message", "Entrer votre code secret~0.Retour ");
            map.put("command", "1");
            map.put("command", "1");

            return map;
        }

        //checker le code ping pour afficher la liste des transactions
        if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100*5*3")) {
            Responses response = new Responses();
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

            map.put("message", response.getMsg() + "~0.Retour ");
            map.put("command", "1");
            return map;
        }

        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*1")) {

            sessionussd.setLastsep("237*100*5");

            map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Changer de langue
        if (pojoUssd.getMessage().equals("4") && sessionussd.getLastsep().equals("237*100*5")) {

            System.out.println(sessionussd.getLastsep());
            sessionussd.setLastsep("237*100*5*4");
            ussdRepository.save(sessionussd);

            map.put("message", "Choix de la langue~1.Français~2.Englais~0.Retour ");
            map.put("command", "1");
            return map;
        }
        //pour retourner au menu principal étant sur le menu service tierce
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5")) {
            sessionussd.setLastsep("237*100");
            ussdRepository.save(sessionussd);

            map.put("message", "Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu mon compte Perfectpay
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*1")) {
            sessionussd.setLastsep("237*100*5");
            ussdRepository.save(sessionussd);

            map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
            map.put("command", "1");
            return map;
        }

        //Si le user entre 0 pour rentrer au menu mon compte Perfectpay
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*2")) {
            sessionussd.setLastsep("237*100*5");
            ussdRepository.save(sessionussd);

            map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
            map.put("command", "1");
            return map;
        }
        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*3")) {
            sessionussd.setLastsep("237*100*5");
            ussdRepository.save(sessionussd);

            map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
            map.put("command", "1");
            return map;
        }

        if (pojoUssd.getMessage().equals("0") && sessionussd.getLastsep().equals("237*100*5*4")) {
            sessionussd.setLastsep("237*100*5");
            ussdRepository.save(sessionussd);

            map.put("message", "Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernieres transactions~4.Changer de langue~0.Retour ");
            map.put("command", "1");
            return map;
        }
        
        
        
        
        
        //Creation de compte ***************************************************************************************************************************************        
      
        if (pojoUssd.getMessage().equals("1") && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0) {
            
            map.put("message", "Entrez le nom~0.Annuler ");
            map.put("command", "1");
            return map;
        }
       //Save nom 
         if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getNom()==null) {
           
            sessionussd.setNom(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez le prenom~0.Annuler ");
            map.put("command", "1");
            return map;
        }
         
         //Save prenom
         if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getNom()!=null) {
           
            sessionussd.setPrenom(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez le Numero de CNI~0.Annuler ");
            map.put("command", "1");
            return map;
        }
         
         //Save CNI
          if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getPrenom()!=null) {
           
            Responses response = new Responses();
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
            sessionussd.setCni(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Choisir le Sexe~1.MAsculin~2.Feminin~0.Annuler ");
            map.put("command", "1");
            return map;
        }
          
           //Save Sexe
          if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getCni()!=null) {
           
            Responses response = new Responses();
            response = ussdservice.CheckSexe(pojoUssd.getMessage());
         
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
            sessionussd.setSexe(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez la Date de Naissance~0.Annuler ");
            map.put("command", "1");
            return map;
        }
          
           //Save Date
          if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getCni()!=null) {
           
            Responses response = new Responses();
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
            sessionussd.setDatenaissance(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez le Lieu de Naissance~0.Annuler ");
            map.put("command", "1");
            return map;
        }
          
          //Save lieu lieu de naissance
           if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getCni()!=null) {
         
            sessionussd.setLieunaissance(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez le Numero de Contribuable~0.Annuler ");
            map.put("command", "1");
            return map;
        }
           
           
          //Save Contribuable 
           
           if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getCni()!=null) {
            Responses response = new Responses();
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
            sessionussd.setNumbcontribuable(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez votre adresse email~0.Annuler ");
            map.put("command", "1");
            return map;
        }
           
           //Save Email       
           if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getCni()!=null) {
            Responses response = new Responses();
            response = ussdservice.CheckEmail(pojoUssd.getMessage());
         
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
            sessionussd.setEmail(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Entrez votre code secret~0.Annuler ");
            map.put("command", "1");
            return map;
        }
           
           //Save code secret
            if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getCni()!=null) {
           
            sessionussd.setCodesecret(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            map.put("message", "Confirmez votre CodeSecret~0.Annuler ");
            map.put("command", "1");
            return map;
        }
            
            //confirmation du code secret
             if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getCni()!=null) {
            Responses response = new Responses();
            response = ussdservice.ConfirmCodeSecret(sessionussd.getCodesecret(),pojoUssd.getMessage());
         
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
             
            sessionussd.setNewcode(pojoUssd.getMessage());
            ussdRepository.save(sessionussd);
            response = new Responses();
            response = ussdservice.CreateAccountUssd(sessionussd));
            map.put("message", response.getMsg()+"~0.Retour ");
            map.put("command", "1");
            return map;
        }
          
         
        
          if (!"0".equals(pojoUssd.getMessage()) && sessionussd.getLastsep().equals("237*100")&& sessionussd.getAccess()==0 && sessionussd.getNom()==null) {
            

            map.put("message", "Entrer le nom~0.Annuler ");
            map.put("command", "1");
            return map;
        }
        
        
        if (pojoUssd.getMessage()
                .equals("0")) {
            map.put("message", "Operation annulee! ");
            map.put("command", "0");
            return map;
        }

        return map;
    }
}
