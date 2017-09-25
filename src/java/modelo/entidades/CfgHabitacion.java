/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Enderson
 */
@Entity
@XmlRootElement
@Table(name = "cfg_habitacion", schema = "public")

public class CfgHabitacion implements Serializable {

    private static final long serialVersionUID = -45454543222217L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_habitacion", nullable = false)
    private Integer idHabitacion;
    @JoinColumn(name = "id_sede")
    @ManyToOne
    private CfgSede cfgSedes;
    @Column(name = "numero_habitacion")
    private String numeroHabitacion;
    @Column(name = "observacion")
    private String observacion;
    @OneToMany(mappedBy = "cfgHabitacion", cascade = CascadeType.ALL)
    private List<CfgCama> cfgCamaList;
    @Column(name = "is_habitacion_urgencia")
    private Boolean isHabitacionUrgencia;

    public CfgHabitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public CfgHabitacion() {
    }

    public Integer getIdHabitacion() {
        return idHabitacion;
    }

    public CfgHabitacion(Integer idHabitacion, CfgSede cfgSedes, String numeroHabitacion, String observacion, List<CfgCama> cfgCamaList) {
        this.idHabitacion = idHabitacion;
        this.cfgSedes = cfgSedes;
        this.numeroHabitacion = numeroHabitacion;
        this.observacion = observacion;
        this.cfgCamaList = cfgCamaList;
    }

    public CfgSede getCfgSedes() {
        return cfgSedes;
    }

    public void setCfgSedes(CfgSede cfgSedes) {
        this.cfgSedes = cfgSedes;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @XmlTransient
    public List<CfgCama> getCfgCamaList() {
        return cfgCamaList;
    }

    public void setCfgCamaList(List<CfgCama> cfgCamaList) {
        this.cfgCamaList = cfgCamaList;
    }

    public Boolean getIsHabitacionUrgencia() {
        return isHabitacionUrgencia;
    }

    public void setIsHabitacionUrgencia(Boolean isHabitacionUrgencia) {
        this.isHabitacionUrgencia = isHabitacionUrgencia;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.idHabitacion);
        hash = 89 * hash + Objects.hashCode(this.cfgSedes);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CfgHabitacion other = (CfgHabitacion) obj;
        if (!Objects.equals(this.idHabitacion, other.idHabitacion)) {
            return false;
        }
        if (!Objects.equals(this.cfgSedes, other.cfgSedes)) {
            return false;
        }
        return true;
    }

}
