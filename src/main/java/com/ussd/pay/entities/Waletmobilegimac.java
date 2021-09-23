/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author chiek
 */
@Entity
@Table(name = "waletmobilegimac")
@NamedQueries({
    @NamedQuery(name = "Waletmobilegimac.findAll", query = "SELECT w FROM Waletmobilegimac w")})
public class Waletmobilegimac implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "waletmobile_id")
    private Integer waletmobileId;
    @Column(name = "paysgimac_id")
    private Integer paysgimacId;
    @Column(name = "libelle_walet")
    private String libelleWalet;
    @Column(name = "code_walet")
    private String codeWalet;
    @Column(name = "mno")
    private String mno;

    public Waletmobilegimac() {
    }

    public Waletmobilegimac(Integer waletmobileId) {
        this.waletmobileId = waletmobileId;
    }

    public Integer getWaletmobileId() {
        return waletmobileId;
    }

    public void setWaletmobileId(Integer waletmobileId) {
        this.waletmobileId = waletmobileId;
    }

    public Integer getPaysgimacId() {
        return paysgimacId;
    }

    public void setPaysgimacId(Integer paysgimacId) {
        this.paysgimacId = paysgimacId;
    }

    public String getLibelleWalet() {
        return libelleWalet;
    }

    public void setLibelleWalet(String libelleWalet) {
        this.libelleWalet = libelleWalet;
    }

    public String getCodeWalet() {
        return codeWalet;
    }

    public void setCodeWalet(String codeWalet) {
        this.codeWalet = codeWalet;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (waletmobileId != null ? waletmobileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Waletmobilegimac)) {
            return false;
        }
        Waletmobilegimac other = (Waletmobilegimac) object;
        if ((this.waletmobileId == null && other.waletmobileId != null) || (this.waletmobileId != null && !this.waletmobileId.equals(other.waletmobileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ussd.pay.entities.Waletmobilegimac[ waletmobileId=" + waletmobileId + " ]";
    }
    
}
