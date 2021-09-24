/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.dao;

import com.ussd.pay.entities.Waletbancairegimac;
import com.ussd.pay.entities.Waletmobilegimac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author chiek
 */
public interface WaletbancaireRepository extends JpaRepository<Waletbancairegimac, Integer> {
    
    @Query("SELECT w FROM Waletbancairegimac w WHERE w.paysgimacId=:idpays AND w.libelleWalet=:libelWalet")
    public Waletbancairegimac findByWaletbancairegimac(@Param("idpays") int
            idpays,@Param("libelWalet")String libelWalet );
}
