/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.controler;

import com.ussd.pay.pojo.PojoUssd;
import java.util.HashMap;
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

    @RequestMapping(value = "/ussd", method = RequestMethod.POST)
    public HashMap getUssd(@RequestBody PojoUssd pojoUssd) {
        HashMap map = new HashMap();
        if (pojoUssd.getMessage().equals("237*100")) {
            map.put("message","Bienvenue sur PerfectPay~1.Transfert d'argent~2.Paiement Marchand~3.Operations Bancaires~4.Services Tiers~5.Mon compte~0.Annuler ");
            map.put("command", "1");
            return map;
        }
        
         if (pojoUssd.getMessage().equals("1")) {
            map.put("message","Transfert d'argent~1.Vers un client PerfectPay~2.Vers un client MTN~3.Vers un client Orange~4.Vers un client EU MobileMoney~5.Vers un client YUP~6.Vers un client YUP~0.Retour ");
            map.put("command", "1");
            return map;
        }
         
          if (pojoUssd.getMessage().equals("2")) {
            map.put("message","Paiement Marchand~1.Nouveau Paiement~2.Checker un code marchand~0.Retour");
            map.put("command", "1");
            return map;
        }
          
           if (pojoUssd.getMessage().equals("3")) {
            map.put("message","Selectionner votre banque parmi nos banques partenaires~1.UBA~2.Afriland~3.Banque Atlantique~4.Ecobank~5.Societe Generale~0.Retour ");
            map.put("command", "1");
            return map;
        }
           
           if (pojoUssd.getMessage().equals("4")) {
            map.put("message","Services Tierces~1.Payez un abonnement GEDOMED~2.Payez une licence IPLANS ERP~3.Payer Cotisation ONMC~4.Payer Frais etude de dossier ONMC~5.Payer Frais de siège ONMC~6.Payer Facture ENEO~7.Payer Facture CAMWATER");
            map.put("command", "1");
            return map;
        }
           
            if (pojoUssd.getMessage().equals("5")) {
            map.put("message","Mon compte PerfectPay~1.Consulter solde~2.Modifier Code PIN~3.Consulter dernières transactions~4.Changer de langue~0.Retour ");
            map.put("command", "1");
            return map;
        }
            
             if (pojoUssd.getMessage().equals("0")) {
            map.put("message","Operation annulee! ");
            map.put("command", "0");
            return map;
        }

        return map;
    }
}
