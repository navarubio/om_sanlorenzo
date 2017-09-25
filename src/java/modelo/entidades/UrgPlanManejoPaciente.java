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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Enderson
 */
@Entity
@XmlRootElement
@Table(name = "urg_plan_manejo_paciente", schema = "public")

public class UrgPlanManejoPaciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_destino", nullable = false)
    private Integer idDestino;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "id_consulta")
    @OneToOne
    private UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "idprestador")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @JoinColumn(name = "ambito_detino_paciente", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones ambitoDestinoPaciente;

    public UrgPlanManejoPaciente() {
    }

    public UrgPlanManejoPaciente(Integer idDestino) {
        this.idDestino = idDestino;
    }

    public UrgPlanManejoPaciente(Integer idDestino, Date hora, Date fecha, UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia, String observacion, CfgUsuarios idPrestador) {
        this.idDestino = idDestino;
        this.hora = hora;
        this.fecha = fecha;
        this.idUrgConsultaPacienteUrgencia = idUrgConsultaPacienteUrgencia;
        this.observacion = observacion;
        this.idPrestador = idPrestador;
    }

    public Integer getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(Integer idDestino) {
        this.idDestino = idDestino;
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

    public UrgConsultaPacienteUrgencia getIdUrgConsultaPacienteUrgencia() {
        return idUrgConsultaPacienteUrgencia;
    }

    public void setIdUrgConsultaPacienteUrgencia(UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia) {
        this.idUrgConsultaPacienteUrgencia = idUrgConsultaPacienteUrgencia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    @XmlTransient
    public CfgClasificaciones getAmbitoDestinoPaciente() {
        return ambitoDestinoPaciente;
    }

    public void setAmbitoDestinoPaciente(CfgClasificaciones ambitoDestinoPaciente) {
        this.ambitoDestinoPaciente = ambitoDestinoPaciente;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.idDestino);
        hash = 59 * hash + Objects.hashCode(this.idUrgConsultaPacienteUrgencia);
        hash = 59 * hash + Objects.hashCode(this.idPrestador);
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
        final UrgPlanManejoPaciente other = (UrgPlanManejoPaciente) obj;
        if (!Objects.equals(this.idDestino, other.idDestino)) {
            return false;
        }
        if (!Objects.equals(this.idUrgConsultaPacienteUrgencia, other.idUrgConsultaPacienteUrgencia)) {
            return false;
        }
        if (!Objects.equals(this.idPrestador, other.idPrestador)) {
            return false;
        }
        return true;
    }

}
