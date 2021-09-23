/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.dao;

import com.ussd.pay.entities.Waletbancairegimac;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author chiek
 */
public interface WaletbancaireRepository extends JpaRepository<Waletbancairegimac, Integer> {
    
}
