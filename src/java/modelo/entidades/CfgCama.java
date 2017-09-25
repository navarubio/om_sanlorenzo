/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Enderson
 */
@Entity
@XmlRootElement
@Table(name = "cfg_cama", schema = "public")

public class CfgCama implements Serializable {

    private static final long serialVersionUID = -45454543222217L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cama", nullable = false)
    private Integer idCama;
    @JoinColumn(name = "id_habitacion")
    @ManyToOne
    private CfgHabitacion cfgHabitacion;
    @Column(name = "numero_cama")
    private String numeroCama;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "ocupado")
    private Boolean ocupado;
    @OneToOne(mappedBy = "cfgCama")
    private UrgConsultaPacienteUrgencia urgConsultaPacienteUrgencia;

    public CfgCama() {
    }

    public CfgCama(Integer idCama) {
        this.idCama = idCama;
    }

    public CfgCama(Integer idCama, CfgHabitacion cfgHabitacion, String numeroCama, String observacion, Boolean ocupado) {
        this.idCama = idCama;
        this.cfgHabitacion = cfgHabitacion;
        this.numeroCama = numeroCama;
        this.observacion = observacion;
        this.ocupado = ocupado;
    }

    public Integer getIdCama() {
        return idCama;
    }

    public void setIdCama(Integer idCama) {
        this.idCama = idCama;
    }

    public CfgHabitacion getCfgHabitacion() {
        return cfgHabitacion;
    }

    public void setCfgHabitacion(CfgHabitacion cfgHabitacion) {
        this.cfgHabitacion = cfgHabitacion;
    }

    public String getNumeroCama() {
        return numeroCama;
    }

    public void setNumeroCama(String numeroCama) {
        this.numeroCama = numeroCama;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getOcupado() {
        return ocupado;
    }

    public void setOcupado(Boolean ocupado) {
        this.ocupado = ocupado;
    }

    public UrgConsultaPacienteUrgencia getUrgConsultaPacienteUrgencia() {
        return urgConsultaPacienteUrgencia;
    }

    public void setUrgConsultaPacienteUrgencia(UrgConsultaPacienteUrgencia urgConsultaPacienteUrgencia) {
        this.urgConsultaPacienteUrgencia = urgConsultaPacienteUrgencia;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idCama);
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
        final CfgCama other = (CfgCama) obj;
        if (!Objects.equals(this.idCama, other.idCama)) {
            return false;
        }
        return true;
    }

}
