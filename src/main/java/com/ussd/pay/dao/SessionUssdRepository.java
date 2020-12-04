/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.dao;

import com.ussd.pay.entities.Sessionussd;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Carlos TCHIOZEM
 */
public interface SessionUssdRepository extends JpaRepository<Sessionussd, Integer>{
    

    @Query(value = "SELECT s FROM Sessionussd s WHERE s.sessionid=?1")
    public Sessionussd findApiBySessionId(String sessionid);
}
