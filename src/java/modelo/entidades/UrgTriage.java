/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import beans.enumeradores.NivelTriage;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
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
@Table(name = "urg_triage", schema = "public")
@NamedQueries({
    @NamedQuery(name = "UrgTriage.findAllPacientesByTriage", query = "SELECT u FROM UrgTriage u WHERE u.idAdmision.estado =:estado or u.idAdmision.estado =:estadoObservacion")
    ,
@NamedQuery(name = "UrgTriage.findAllHistorialUrgencias", query = "SELECT u FROM UrgTriage u WHERE  u.idAdmision.estado =:estadoObservacion")})

public class UrgTriage implements Serializable {

    private static final long serialVersionUID = -4877644275349390160L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_triage", nullable = false)
    private Integer idTriage;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "hora_triage")
    @Temporal(TemporalType.TIME)
    private Date horaTriage;
    @Column(name = "fecha_triage")
    @Temporal(TemporalType.DATE)
    private Date fechaTriage;
    @JoinColumn(name = "idPrestador")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @JoinColumn(name = "id_admision")
    @OneToOne(optional = false)
    private UrgAdmision idAdmision;
    @OneToOne(mappedBy = "idUrgTriage")
    private UrgConsultaPacienteUrgencia IdUrgConsultaPacienteUrgencia;
    @Column(name = "peso")
    private Double peso;
    @Column(name = "talla")
    private Double talla;
    @Column(name = "mc")
    private Double mc;
    @Column(name = "temperatura")
    private Double temperatura;
    @Column(name = "frecuencia_cardiaca")
    private Double frecuenciaCardiaca;
    @Column(name = "frecuencia_respiratoria")
    private Double frecuencia_respiratoria;
    @Column(name = "presion_arterial_minima")
    private Double presionArteriaMinima;
    @Column(name = "presion_arterial_maxima")
    private Double presionArteriaMaxima;
    @Column(name = "saturacion")
    private Double saturacion;
    @Column(name = "hallazgos_clinico")
    private String hallazgosClinico;
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_triage")
    private NivelTriage nivelTriage;
    @Column(name = "conducta")
    private String conducta;
    @JoinColumn(name = "impresion_diagnostica", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones impresionDiagnostica;

    public UrgTriage() {
    }

    public UrgTriage(Integer idTriage) {
        this.idTriage = idTriage;
    }

    public UrgTriage(Integer idTriage, String motivo, Date horaTriage, Date fechaTriage, CfgUsuarios idPrestador, UrgAdmision idAdmision, UrgConsultaPacienteUrgencia IdUrgConsultaPacienteUrgencia, Double peso, Double talla, Double mc, Double temperatura, Double frecuenciaCardiaca, Double frecuencia_respiratoria, Double presionArteriaMinima, Double presionArteriaMaxima, Double saturacion, String hallazgosClinico, NivelTriage nivelTriage, String conducta, CfgClasificaciones impresionDiagnostica) {
        this.idTriage = idTriage;
        this.motivo = motivo;
        this.horaTriage = horaTriage;
        this.fechaTriage = fechaTriage;
        this.idPrestador = idPrestador;
        this.idAdmision = idAdmision;
        this.IdUrgConsultaPacienteUrgencia = IdUrgConsultaPacienteUrgencia;
        this.peso = peso;
        this.talla = talla;
        this.mc = mc;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.frecuencia_respiratoria = frecuencia_respiratoria;
        this.presionArteriaMinima = presionArteriaMinima;
        this.presionArteriaMaxima = presionArteriaMaxima;
        this.saturacion = saturacion;
        this.hallazgosClinico = hallazgosClinico;
        this.nivelTriage = nivelTriage;
        this.conducta = conducta;
        this.impresionDiagnostica = impresionDiagnostica;
    }

    public Integer getIdTriage() {
        return idTriage;
    }

    public void setIdTriage(Integer idTriage) {
        this.idTriage = idTriage;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getHoraTriage() {
        return horaTriage;
    }

    public void setHoraTriage(Date horaTriage) {
        this.horaTriage = horaTriage;
    }

    public Date getFechaTriage() {
        return fechaTriage;
    }

    public void setFechaTriage(Date fechaTriage) {
        this.fechaTriage = fechaTriage;
    }

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    public UrgAdmision getIdAdmision() {
        return idAdmision;
    }

    public void setIdAdmision(UrgAdmision idAdmision) {
        this.idAdmision = idAdmision;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getTalla() {
        return talla;
    }

    public void setTalla(Double talla) {
        this.talla = talla;
    }

    public Double getMc() {
        return mc;
    }

    public void setMc(Double mc) {
        this.mc = mc;
    }

    public Double getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(Double frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public Double getFrecuencia_respiratoria() {
        return frecuencia_respiratoria;
    }

    public void setFrecuencia_respiratoria(Double frecuencia_respiratoria) {
        this.frecuencia_respiratoria = frecuencia_respiratoria;
    }

    public Double getPresionArteriaMinima() {
        return presionArteriaMinima;
    }

    public void setPresionArteriaMinima(Double presionArteriaMinima) {
        this.presionArteriaMinima = presionArteriaMinima;
    }

    public Double getPresionArteriaMaxima() {
        return presionArteriaMaxima;
    }

    public void setPresionArteriaMaxima(Double presionArteriaMaxima) {
        this.presionArteriaMaxima = presionArteriaMaxima;
    }

    public Double getSaturacion() {
        return saturacion;
    }

    public void setSaturacion(Double saturacion) {
        this.saturacion = saturacion;
    }

    public String getHallazgosClinico() {
        return hallazgosClinico;
    }

    public void setHallazgosClinico(String hallazgosClinico) {
        this.hallazgosClinico = hallazgosClinico;
    }

    public NivelTriage getNivelTriage() {
        return nivelTriage;
    }

    public void setNivelTriage(NivelTriage nivelTriage) {
        this.nivelTriage = nivelTriage;
    }

    public String getConducta() {
        return conducta;
    }

    public void setConducta(String conducta) {
        this.conducta = conducta;
    }

    public CfgClasificaciones getImpresionDiagnostica() {
        return impresionDiagnostica;
    }

    public void setImpresionDiagnostica(CfgClasificaciones impresionDiagnostica) {
        this.impresionDiagnostica = impresionDiagnostica;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public UrgConsultaPacienteUrgencia getIdUrgConsultaPacienteUrgencia() {
        return IdUrgConsultaPacienteUrgencia;
    }

    public void setIdUrgConsultaPacienteUrgencia(UrgConsultaPacienteUrgencia IdUrgConsultaPacienteUrgencia) {
        this.IdUrgConsultaPacienteUrgencia = IdUrgConsultaPacienteUrgencia;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.idTriage);
        hash = 71 * hash + Objects.hashCode(this.idPrestador);
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
        final UrgTriage other = (UrgTriage) obj;
        if (!Objects.equals(this.idTriage, other.idTriage)) {
            return false;
        }
        if (!Objects.equals(this.idPrestador, other.idPrestador)) {
            return false;
        }
        return true;
    }

}
