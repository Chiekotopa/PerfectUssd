/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.dao;

import com.ussd.pay.entities.Sessiontrans;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Carlos TCHIOZEM
 */
public interface SessiontransRepository extends JpaRepository<Sessiontrans, Integer> {
    
    @Query("SELECT ss FROM Sessiontrans ss WHERE ss.phonedestinataire=?1 AND ss.status='1'")
    public Sessiontrans findBySessiontrans(String teldest);
    
     @Query("SELECT ss FROM Sessiontrans ss WHERE ss.phonedestinataire=?1 AND ss.codesecret='wait'")
    public Sessiontrans findSessiontransBySecretcode(String teldest);
    
     
     @Query("SELECT ss FROM Sessiontrans ss WHERE ss.phonedestinataire=?1 AND ss.phoneagent=?2")
    public Sessiontrans findListSessiontransBySecretcode(String teldest,String telExp);
        
    
}
