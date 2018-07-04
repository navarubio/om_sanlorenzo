/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sismacontab
 */
@Entity
@Table(name = "hc_anexos_3047", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HcAnexos3047.findAll", query = "SELECT h FROM HcAnexos3047 h"),
    @NamedQuery(name = "HcAnexos3047.findByIdAnexos3047", query = "SELECT h FROM HcAnexos3047 h WHERE h.idAnexos3047 = :idAnexos3047"),
    @NamedQuery(name = "HcAnexos3047.findByCodAnexo", query = "SELECT h FROM HcAnexos3047 h WHERE h.codanexo= :codanexo"),
    @NamedQuery(name = "HcAnexos3047.findByNombre", query = "SELECT h FROM HcAnexos3047 h WHERE h.nombre = :nombre"),
    @NamedQuery(name = "HcAnexos3047.findByUrlPagina", query = "SELECT h FROM HcAnexos3047 h WHERE h.urlPagina = :urlPagina"),
    @NamedQuery(name = "HcAnexos3047.findByActivo", query = "SELECT h FROM HcAnexos3047 h WHERE h.activo = :activo"),
    @NamedQuery(name = "HcAnexos3047.findByCantCampos", query = "SELECT h FROM HcAnexos3047 h WHERE h.cantCampos = :cantCampos"),
    @NamedQuery(name = "HcAnexos3047.findByConsecutivo", query = "SELECT h FROM HcAnexos3047 h WHERE h.consecutivo = :consecutivo")})
public class HcAnexos3047 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_anexos_3047", nullable = false)
    private Integer idAnexos3047;
    @Column(name = "cod_anexo", length = 7)
    private String codanexo;
    @Column(name = "nombre", length = 100)
    private String nombre;
    @Column(name = "url_pagina", length = 200)
    private String urlPagina;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "cant_campos")
    private Integer cantCampos;
    @Column(name = "consecutivo")
    private Integer consecutivo;
//    @OneToMany(mappedBy = "idAnexos3047")
//    private List<HcCamposReg> hcCamposRegList;
//    @OneToMany(mappedBy = "idAnexos3047")
//    private List<HcRegistro> hcRegistroList;

    public HcAnexos3047() {
    }

    public HcAnexos3047(Integer idAnexos3047) {
        this.idAnexos3047 = idAnexos3047;
    }

    public Integer getIdAnexos3047() {
        return idAnexos3047;
    }

    public void setIdAnexos3047(Integer idAnexos3047) {
        this.idAnexos3047 = idAnexos3047;
    }

    public String getCodanexo() {
        return codanexo;
    }

    public void setCodanexo(String codanexo) {
        this.codanexo = codanexo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlPagina() {
        return urlPagina;
    }

    public void setUrlPagina(String urlPagina) {
        this.urlPagina = urlPagina;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
     public Integer getCantCampos() {
        return cantCampos;
    }

    public void setCantCampos(Integer cantCampos) {
        this.cantCampos = cantCampos;
    }

    public Integer getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
    }

//    @XmlTransient
//    public List<HcCamposReg> getHcCamposRegList() {
//        return hcCamposRegList;
//    }
//
//    public void setHcCamposRegList(List<HcCamposReg> hcCamposRegList) {
//        this.hcCamposRegList = hcCamposRegList;
//    }
//
//    @XmlTransient
//    public List<HcRegistro> getHcRegistroList() {
//        return hcRegistroList;
//    }
//
//    public void setHcRegistroList(List<HcRegistro> hcRegistroList) {
//        this.hcRegistroList = hcRegistroList;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnexos3047 != null ? idAnexos3047.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HcAnexos3047)) {
            return false;
        }
        HcAnexos3047 other = (HcAnexos3047) object;
        if ((this.idAnexos3047 == null && other.idAnexos3047 != null) || (this.idAnexos3047 != null && !this.idAnexos3047.equals(other.idAnexos3047))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades2.HcAnexos3047[ idAnexos3047=" + idAnexos3047 + " ]";
    }
    
}
