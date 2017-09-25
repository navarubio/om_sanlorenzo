/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import beans.enumeradores.IndicacionesMedicamentos;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "urg_prescripcion_medicamentos", schema = "public")

public class UrgPrescripcionMedicamento implements Serializable {

    private static final long serialVersionUID = -45454543222217L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_prescripcion", nullable = false)
    private Integer idPrescripcion;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "id_urgconsulta_paciente_urgencia")
    @ManyToOne
    private UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia;
    @JoinColumn(name = "idprestador")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @JoinColumn(name = "idmedicamento")
    @ManyToOne
    private CfgMedicamento idMedicamento;
    @Column(name = "indicaciones_medicamentos")
    @Enumerated(EnumType.STRING)
    private IndicacionesMedicamentos indicacionesMedicamentos;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "suministrado")
    private boolean suministrado;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "cantidad_suministrada")
    private Integer cantidadSuministrada;
    @OneToMany(mappedBy = "urgPrescripcionMedicamento")
    private List<UrgControlPrescripcionMedicamento> urgControlPrescripcionMedicamentoList;
    @Column(name = "dosis")
    private Integer dosis;

    public UrgPrescripcionMedicamento() {
    }

    public UrgPrescripcionMedicamento(Integer idPrescripcion) {
        this.idPrescripcion = idPrescripcion;
    }

    public UrgPrescripcionMedicamento(Integer idPrescripcion, Date hora, Date fecha, UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia, CfgUsuarios idPrestador, CfgMedicamento idMedicamento, IndicacionesMedicamentos indicacionesMedicamentos, String observacion, boolean suministrado) {
        this.idPrescripcion = idPrescripcion;
        this.hora = hora;
        this.fecha = fecha;
        this.idUrgConsultaPacienteUrgencia = idUrgConsultaPacienteUrgencia;
        this.idPrestador = idPrestador;
        this.idMedicamento = idMedicamento;
        this.indicacionesMedicamentos = indicacionesMedicamentos;
        this.observacion = observacion;
        this.suministrado = suministrado;
    }

    public Integer getIdPrescripcion() {
        return idPrescripcion;
    }

    public void setIdPrescripcion(Integer idPrescripcion) {
        this.idPrescripcion = idPrescripcion;
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

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    public CfgMedicamento getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(CfgMedicamento idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public IndicacionesMedicamentos getIndicacionesMedicamentos() {
        return indicacionesMedicamentos;
    }

    public void setIndicacionesMedicamentos(IndicacionesMedicamentos indicacionesMedicamentos) {
        this.indicacionesMedicamentos = indicacionesMedicamentos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean isSuministrado() {
        return suministrado;
    }

    public void setSuministrado(boolean suministrado) {
        this.suministrado = suministrado;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidadSuministrada() {
        return cantidadSuministrada;
    }

    public void setCantidadSuministrada(Integer cantidadSuministrada) {
        this.cantidadSuministrada = cantidadSuministrada;
    }

    @XmlTransient
    public List<UrgControlPrescripcionMedicamento> getUrgControlPrescripcionMedicamentoList() {
        return urgControlPrescripcionMedicamentoList;
    }

    public void setUrgControlPrescripcionMedicamentoList(List<UrgControlPrescripcionMedicamento> urgControlPrescripcionMedicamentoList) {
        this.urgControlPrescripcionMedicamentoList = urgControlPrescripcionMedicamentoList;
    }

    public Integer getDosis() {
        return dosis;
    }

    public void setDosis(Integer dosis) {
        this.dosis = dosis;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.idPrescripcion);
        hash = 97 * hash + Objects.hashCode(this.idUrgConsultaPacienteUrgencia);
        hash = 97 * hash + Objects.hashCode(this.idMedicamento);
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
        final UrgPrescripcionMedicamento other = (UrgPrescripcionMedicamento) obj;
        if (!Objects.equals(this.idPrescripcion, other.idPrescripcion)) {
            return false;
        }
        if (!Objects.equals(this.idUrgConsultaPacienteUrgencia, other.idUrgConsultaPacienteUrgencia)) {
            return false;
        }
        if (!Objects.equals(this.idMedicamento, other.idMedicamento)) {
            return false;
        }
        return true;
    }

}
