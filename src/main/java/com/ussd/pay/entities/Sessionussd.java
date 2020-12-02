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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos TCHIOZEM
 */
@Entity
@Table(name = "sessionussd")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sessionussd.findAll", query = "SELECT s FROM Sessionussd s")})
public class Sessionussd implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idussd")
    private Integer idussd;
    @Column(name = "provider")
    private String provider;
    @Column(name = "msisdn")
    private String msisdn;
    @Column(name = "sessionid")
    private String sessionid;
    @Column(name = "message")
    private String message;
    @Column(name = "lastsep")
    private String lastsep;
    @Column(name = "destinataire")
    private String destinataire;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "codesecret")
    private String codesecret;

    public Sessionussd() {
    }

    public Sessionussd(Integer idussd) {
        this.idussd = idussd;
    }

    public Integer getIdussd() {
        return idussd;
    }

    public void setIdussd(Integer idussd) {
        this.idussd = idussd;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastsep() {
        return lastsep;
    }

    public void setLastsep(String lastsep) {
        this.lastsep = lastsep;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getCodesecret() {
        return codesecret;
    }

    public void setCodesecret(String codesecret) {
        this.codesecret = codesecret;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idussd != null ? idussd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sessionussd)) {
            return false;
        }
        Sessionussd other = (Sessionussd) object;
        if ((this.idussd == null && other.idussd != null) || (this.idussd != null && !this.idussd.equals(other.idussd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ussd.pay.entities.Sessionussd[ idussd=" + idussd + " ]";
    }
    
}
