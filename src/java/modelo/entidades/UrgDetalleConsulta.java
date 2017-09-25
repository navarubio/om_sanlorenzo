/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import beans.enumeradores.EstadoFisicoPaciente;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "urg_detalle_consulta_paciente", schema = "public")

public class UrgDetalleConsulta implements Serializable {

    private static final long serialVersionUID = -311012958467629557L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_detalleconsulta", nullable = false)
    private Integer idDetalleConsulta;
    @Column(name = "hora_iniconsulta")
    @Temporal(TemporalType.TIME)
    private Date horaIniConsulta;
    @Column(name = "fecha_iniconsulta")
    @Temporal(TemporalType.DATE)
    private Date fechaIniConsulta;
    @Column(name = "hora_finconsulta")
    @Temporal(TemporalType.TIME)
    private Date horaFinConsulta;
    @Column(name = "fecha_finconsulta")
    @Temporal(TemporalType.DATE)
    private Date fechaFinConsulta;
    @JoinColumn(name = "id_urgconsulta_paciente_urgencia")
    @OneToOne
    private UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia;
    @JoinColumn(name = "idPrestador")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @Column(name = "revision")
    private String revision;
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
    @Column(name = "observacion")
    private String observacion;

    @Column(name = "cabeza")
    private String cabeza;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cabeza")
    private EstadoFisicoPaciente estadoCabeza;
    @Column(name = "cuello")
    private String cuello;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuello")
    private EstadoFisicoPaciente estadoCuello;
    @Column(name = "torax")
    private String torax;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_torax")
    private EstadoFisicoPaciente estadoTorax;
    @Column(name = "abdomen")
    private String abdomen;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_abdomen")
    private EstadoFisicoPaciente estadoAbdomen;
    @Column(name = "gu")
    private String gu;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_gu")
    private EstadoFisicoPaciente estadoGu;
    @Column(name = "extremidades")
    private String extremidades;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_extremidades")
    private EstadoFisicoPaciente estadoExtremidades;
    @Column(name = "neurologico")
    private String neurologico;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_neurologico")
    private EstadoFisicoPaciente estadoNeurologico;
    @Column(name = "nariz")
    private String nariz;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nariz")
    private EstadoFisicoPaciente estadoNariz;
    @Column(name = "oidos")
    private String oidos;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_oidos")
    private EstadoFisicoPaciente estadoOidos;
    @Column(name = "boca")
    private String boca;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_boca")
    private EstadoFisicoPaciente estadoBoca;
    @Column(name = "ojos")
    private String ojos;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_ojos")
    private EstadoFisicoPaciente estadoOjos;
    @Column(name = "piel")
    private String piel;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_piel")
    private EstadoFisicoPaciente estadoPiel;
    @Column(name = "ano")
    private String ano;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_ano")
    private EstadoFisicoPaciente estadoAno;
    @Column(name = "osteomuscular")
    private String osteomuscular;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_osteomuscular")
    private EstadoFisicoPaciente estadoOsteomuscular;
    @Column(name = "idcfgdiagnostico")
    private String idCfgDiagnostico;
    @JoinColumn(name = "ambito_detino_paciente", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones ambitoDestinoPaciente;
    @Column(name = "recomendacion")
    private String recomendacion;
    @JoinColumn(name = "idregistroauditoria")
    @ManyToOne
    private CfgUsuarios idRegistroAuditoriaProfesional;
    public UrgDetalleConsulta() {
    }

    public Integer getIdDetalleConsulta() {
        return idDetalleConsulta;
    }

    public UrgDetalleConsulta(Integer idDetalleConsulta) {
        this.idDetalleConsulta = idDetalleConsulta;
    }

    public UrgDetalleConsulta(Integer idDetalleConsulta, UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia) {
        this.idDetalleConsulta = idDetalleConsulta;
        this.idUrgConsultaPacienteUrgencia = idUrgConsultaPacienteUrgencia;
    }

    public void setIdDetalleConsulta(Integer idDetalleConsulta) {
        this.idDetalleConsulta = idDetalleConsulta;
    }

    public Date getHoraIniConsulta() {
        return horaIniConsulta;
    }

    public void setHoraIniConsulta(Date horaIniConsulta) {
        this.horaIniConsulta = horaIniConsulta;
    }

    public Date getFechaIniConsulta() {
        return fechaIniConsulta;
    }

    public void setFechaIniConsulta(Date fechaIniConsulta) {
        this.fechaIniConsulta = fechaIniConsulta;
    }

    public Date getHoraFinConsulta() {
        return horaFinConsulta;
    }

    public void setHoraFinConsulta(Date horaFinConsulta) {
        this.horaFinConsulta = horaFinConsulta;
    }

    public Date getFechaFinConsulta() {
        return fechaFinConsulta;
    }

    public void setFechaFinConsulta(Date fechaFinConsulta) {
        this.fechaFinConsulta = fechaFinConsulta;
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

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
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

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
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

    public String getCabeza() {
        return cabeza;
    }

    public void setCabeza(String cabeza) {
        this.cabeza = cabeza;
    }

    public EstadoFisicoPaciente getEstadoCabeza() {
        return estadoCabeza;
    }

    public void setEstadoCabeza(EstadoFisicoPaciente estadoCabeza) {
        this.estadoCabeza = estadoCabeza;
    }

    public String getCuello() {
        return cuello;
    }

    public void setCuello(String cuello) {
        this.cuello = cuello;
    }

    public EstadoFisicoPaciente getEstadoCuello() {
        return estadoCuello;
    }

    public void setEstadoCuello(EstadoFisicoPaciente estadoCuello) {
        this.estadoCuello = estadoCuello;
    }

    public String getTorax() {
        return torax;
    }

    public void setTorax(String torax) {
        this.torax = torax;
    }

    public EstadoFisicoPaciente getEstadoTorax() {
        return estadoTorax;
    }

    public void setEstadoTorax(EstadoFisicoPaciente estadoTorax) {
        this.estadoTorax = estadoTorax;
    }

    public String getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(String abdomen) {
        this.abdomen = abdomen;
    }

    public EstadoFisicoPaciente getEstadoAbdomen() {
        return estadoAbdomen;
    }

    public void setEstadoAbdomen(EstadoFisicoPaciente estadoAbdomen) {
        this.estadoAbdomen = estadoAbdomen;
    }

    public String getGu() {
        return gu;
    }

    public void setGu(String gu) {
        this.gu = gu;
    }

    public EstadoFisicoPaciente getEstadoGu() {
        return estadoGu;
    }

    public void setEstadoGu(EstadoFisicoPaciente estadoGu) {
        this.estadoGu = estadoGu;
    }

    public String getExtremidades() {
        return extremidades;
    }

    public void setExtremidades(String extremidades) {
        this.extremidades = extremidades;
    }

    public EstadoFisicoPaciente getEstadoExtremidades() {
        return estadoExtremidades;
    }

    public void setEstadoExtremidades(EstadoFisicoPaciente estadoExtremidades) {
        this.estadoExtremidades = estadoExtremidades;
    }

    public String getNeurologico() {
        return neurologico;
    }

    public void setNeurologico(String neurologico) {
        this.neurologico = neurologico;
    }

    public EstadoFisicoPaciente getEstadoNeurologico() {
        return estadoNeurologico;
    }

    public void setEstadoNeurologico(EstadoFisicoPaciente estadoNeurologico) {
        this.estadoNeurologico = estadoNeurologico;
    }

    public String getNariz() {
        return nariz;
    }

    public void setNariz(String nariz) {
        this.nariz = nariz;
    }

    public EstadoFisicoPaciente getEstadoNariz() {
        return estadoNariz;
    }

    public void setEstadoNariz(EstadoFisicoPaciente estadoNariz) {
        this.estadoNariz = estadoNariz;
    }

    public String getOidos() {
        return oidos;
    }

    public void setOidos(String oidos) {
        this.oidos = oidos;
    }

    public EstadoFisicoPaciente getEstadoOidos() {
        return estadoOidos;
    }

    public void setEstadoOidos(EstadoFisicoPaciente estadoOidos) {
        this.estadoOidos = estadoOidos;
    }

    public String getBoca() {
        return boca;
    }

    public void setBoca(String boca) {
        this.boca = boca;
    }

    public EstadoFisicoPaciente getEstadoBoca() {
        return estadoBoca;
    }

    public void setEstadoBoca(EstadoFisicoPaciente estadoBoca) {
        this.estadoBoca = estadoBoca;
    }

    public String getOjos() {
        return ojos;
    }

    public void setOjos(String ojos) {
        this.ojos = ojos;
    }

    public EstadoFisicoPaciente getEstadoOjos() {
        return estadoOjos;
    }

    public void setEstadoOjos(EstadoFisicoPaciente estadoOjos) {
        this.estadoOjos = estadoOjos;
    }

    public String getPiel() {
        return piel;
    }

    public void setPiel(String piel) {
        this.piel = piel;
    }

    public EstadoFisicoPaciente getEstadoPiel() {
        return estadoPiel;
    }

    public void setEstadoPiel(EstadoFisicoPaciente estadoPiel) {
        this.estadoPiel = estadoPiel;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getIdCfgDiagnostico() {
        return idCfgDiagnostico;
    }

    public void setIdCfgDiagnostico(String idCfgDiagnostico) {
        this.idCfgDiagnostico = idCfgDiagnostico;
    }

    public EstadoFisicoPaciente getEstadoAno() {
        return estadoAno;
    }

    public void setEstadoAno(EstadoFisicoPaciente estadoAno) {
        this.estadoAno = estadoAno;
    }

    public String getOsteomuscular() {
        return osteomuscular;
    }

    public void setOsteomuscular(String osteomuscular) {
        this.osteomuscular = osteomuscular;
    }

    public EstadoFisicoPaciente getEstadoOsteomuscular() {
        return estadoOsteomuscular;
    }

    public void setEstadoOsteomuscular(EstadoFisicoPaciente estadoOsteomuscular) {
        this.estadoOsteomuscular = estadoOsteomuscular;
    }

    public CfgClasificaciones getAmbitoDestinoPaciente() {
        return ambitoDestinoPaciente;
    }

    public void setAmbitoDestinoPaciente(CfgClasificaciones ambitoDestinoPaciente) {
        this.ambitoDestinoPaciente = ambitoDestinoPaciente;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    public CfgUsuarios getIdRegistroAuditoriaProfesional() {
        return idRegistroAuditoriaProfesional;
    }

    public void setIdRegistroAuditoriaProfesional(CfgUsuarios idRegistroAuditoriaProfesional) {
        this.idRegistroAuditoriaProfesional = idRegistroAuditoriaProfesional;
    }


}
