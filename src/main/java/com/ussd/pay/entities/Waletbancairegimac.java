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
@Table(name = "waletbancairegimac")
@NamedQueries({
    @NamedQuery(name = "Waletbancairegimac.findAll", query = "SELECT w FROM Waletbancairegimac w")})
public class Waletbancairegimac implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "waletbancaire_id")
    private Integer waletbancaireId;
    @Column(name = "paysgimac_id")
    private Integer paysgimacId;
    @Column(name = "libelle_walet")
    private String libelleWalet;
    @Column(name = "code_walet")
    private String codeWalet;
    @Column(name = "banque")
    private String banque;

    public Waletbancairegimac() {
    }

    public Waletbancairegimac(Integer waletbancaireId) {
        this.waletbancaireId = waletbancaireId;
    }

    public Integer getWaletbancaireId() {
        return waletbancaireId;
    }

    public void setWaletbancaireId(Integer waletbancaireId) {
        this.waletbancaireId = waletbancaireId;
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

    public String getBanque() {
        return banque;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (waletbancaireId != null ? waletbancaireId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Waletbancairegimac)) {
            return false;
        }
        Waletbancairegimac other = (Waletbancairegimac) object;
        if ((this.waletbancaireId == null && other.waletbancaireId != null) || (this.waletbancaireId != null && !this.waletbancaireId.equals(other.waletbancaireId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ussd.pay.entities.Waletbancairegimac[ waletbancaireId=" + waletbancaireId + " ]";
    }
    
}
