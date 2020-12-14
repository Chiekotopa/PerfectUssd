/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.dao;

import com.ussd.pay.entities.Sessiontrans;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Carlos TCHIOZEM
 */
public interface SessiontransRepository extends JpaRepository<Sessiontrans, Integer> {
    
}
