/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ussd.pay.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author chiek
 */
@Entity
@Table(name = "sessionussd")

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
    @Column(name = "newcode")
    private String newcode;
    @Column(name = "codemarchant")
    private String codemarchant;
    @Column(name = "access")
    private String access;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "Cni")
    private String cni;
    @Column(name = "Sexe")
    private String sexe;
    @Column(name = "datenaissance")
    private String datenaissance;
    @Column(name = "lieunaissance")
    private String lieunaissance;
    @Column(name = "numbcontribuable")
    private String numbcontribuable;

    @Column(name = "raisontransfert")
    private String raisonTransfert;
    @Column(name = "email")
    private String email;
    @Column(name = "type")
    private String type;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

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

    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public String getCodemarchant() {
        return codemarchant;
    }

    public void setCodemarchant(String codemarchant) {
        this.codemarchant = codemarchant;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(String datenaissance) {
        this.datenaissance = datenaissance;
    }

    public String getLieunaissance() {
        return lieunaissance;
    }

    public void setLieunaissance(String lieunaissance) {
        this.lieunaissance = lieunaissance;
    }

    public String getNumbcontribuable() {
        return numbcontribuable;
    }

    public void setNumbcontribuable(String numbcontribuable) {
        this.numbcontribuable = numbcontribuable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRaisonTransfert() {
        return raisonTransfert;
    }

    public void setRaisonTransfert(String raisonTransfert) {
        this.raisonTransfert = raisonTransfert;
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
