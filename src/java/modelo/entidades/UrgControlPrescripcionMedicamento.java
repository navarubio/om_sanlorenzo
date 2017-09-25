/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Enderson
 */
@Entity
@XmlRootElement
@Table(name = "urg_control_prescripcion_medicamentos", schema = "public")

public class UrgControlPrescripcionMedicamento implements Serializable {

    private static final long serialVersionUID = -45454543222217L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_control_prescripcion", nullable = false)
    private Integer idControlPrescripcion;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "urg_prescripcion_medicamento")
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private UrgPrescripcionMedicamento urgPrescripcionMedicamento;
    @JoinColumn(name = "idprestador")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "cantidad_suministrada")
    private Integer cantidadSuministrada;

    public UrgControlPrescripcionMedicamento() {
    }

    public UrgControlPrescripcionMedicamento(Integer idControlPrescripcion, Date hora, Date fecha, UrgPrescripcionMedicamento urgPrescripcionMedicamento, CfgUsuarios idPrestador, String observacion, Integer cantidadSuministrada) {
        this.idControlPrescripcion = idControlPrescripcion;
        this.hora = hora;
        this.fecha = fecha;
        this.urgPrescripcionMedicamento = urgPrescripcionMedicamento;
        this.idPrestador = idPrestador;
        this.observacion = observacion;
        this.cantidadSuministrada = cantidadSuministrada;
    }

    public UrgControlPrescripcionMedicamento(Integer idControlPrescripcion) {
        this.idControlPrescripcion = idControlPrescripcion;
    }

    public Integer getIdControlPrescripcion() {
        return idControlPrescripcion;
    }

    public void setIdControlPrescripcion(Integer idControlPrescripcion) {
        this.idControlPrescripcion = idControlPrescripcion;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public UrgPrescripcionMedicamento getUrgPrescripcionMedicamento() {
        return urgPrescripcionMedicamento;
    }

    public void setUrgPrescripcionMedicamento(UrgPrescripcionMedicamento urgPrescripcionMedicamento) {
        this.urgPrescripcionMedicamento = urgPrescripcionMedicamento;
    }

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getCantidadSuministrada() {
        return cantidadSuministrada;
    }

    public void setCantidadSuministrada(Integer cantidadSuministrada) {
        this.cantidadSuministrada = cantidadSuministrada;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idControlPrescripcion);
        hash = 79 * hash + Objects.hashCode(this.urgPrescripcionMedicamento);
        hash = 79 * hash + Objects.hashCode(this.idPrestador);
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
        final UrgControlPrescripcionMedicamento other = (UrgControlPrescripcionMedicamento) obj;
        if (!Objects.equals(this.idControlPrescripcion, other.idControlPrescripcion)) {
            return false;
        }
        if (!Objects.equals(this.urgPrescripcionMedicamento, other.urgPrescripcionMedicamento)) {
            return false;
        }
        if (!Objects.equals(this.idPrestador, other.idPrestador)) {
            return false;
        }
        return true;
    }

}
