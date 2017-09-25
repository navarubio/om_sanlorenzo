/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
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
@Table(name = "urg_consulta_paciente", schema = "public")
@NamedQueries({
    @NamedQuery(name = "UrgAdmision.numeroAdmisionesDiarias", query = "SELECT COUNT(u) FROM UrgAdmision u WHERE u.fechaAdmision=:fecha")
    ,
    @NamedQuery(name = "UrgAdmision.findAllAdmisionesVigentes", query = "SELECT u FROM UrgAdmision u WHERE u.estado=:estadoPaciente")})

public class UrgConsultaPacienteUrgencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consulta", nullable = false)
    private Integer idConsulta;
    @Column(name = "atendida")
    private Boolean atendida;
    @Column(name = "motivo")
    private String motivo;
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
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente", nullable = false)
    @ManyToOne(optional = false)
    private CfgPacientes idPaciente;
    @JoinColumn(name = "id_urgtriage")
    @OneToOne(cascade = CascadeType.MERGE)
    private UrgTriage idUrgTriage;
    @JoinColumn(name = "idPrestador")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @Column(name = "enfermedad_actual")
    private String enfermedadActual;
    @ManyToOne
    @JoinColumn(name = "tipocita")
    private CfgClasificaciones tipoCita;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
    @ManyToOne
    private FacServicio idServicio;
    @OneToOne(mappedBy = "idUrgConsultaPacienteUrgencia", cascade = CascadeType.ALL)
    private UrgDetalleConsulta idUrgDetalleConsulta;
    @OneToMany(mappedBy = "idUrgConsultaPacienteUrgencia", cascade = CascadeType.ALL)
    private List<UrgPrescripcionMedicamento> urgPrescripcionMedicamentolist;
    @OneToOne(mappedBy = "idUrgConsultaPacienteUrgencia", cascade = CascadeType.ALL)
    private UrgOrdenCobro urgOrdenCobro;
    @OneToMany(mappedBy = "idUrgConsultaPacienteUrgencia", cascade = CascadeType.ALL)
    private List<UrgNotasMedicas> urgNotasMedicasList;
    @OneToMany(mappedBy = "idUrgConsultaPacienteUrgencia", cascade = CascadeType.ALL)
    private List<UrgNotasEnfermerias> urgNotasEnfermeriaList;
    @OneToOne(mappedBy = "idUrgConsultaPacienteUrgencia", fetch = FetchType.LAZY)
    private UrgPlanManejoPaciente urgDestinoPaciente;
    @JoinColumn(name = "id_cama")
    @OneToOne(cascade = CascadeType.MERGE)
    private CfgCama cfgCama;

    public UrgConsultaPacienteUrgencia() {
    }

    public UrgConsultaPacienteUrgencia(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public UrgConsultaPacienteUrgencia(Integer idConsulta, Boolean atendida, String motivo, Date horaIniConsulta, Date fechaIniConsulta, Date horaFinConsulta, Date fechaFinConsulta, CfgPacientes idPaciente, UrgTriage idUrgTriage, CfgUsuarios idPrestador, String enfermedadActual, CfgClasificaciones tipoCita, FacServicio idServicio, UrgDetalleConsulta idUrgDetalleConsulta, List<UrgPrescripcionMedicamento> urgPrescripcionMedicamentolist, UrgOrdenCobro urgOrdenCobro, List<UrgNotasMedicas> urgNotasMedicasList, List<UrgNotasEnfermerias> urgNotasEnfermeriaList, UrgPlanManejoPaciente UrgDestinoPaciente) {
        this.idConsulta = idConsulta;
        this.atendida = atendida;
        this.motivo = motivo;
        this.horaIniConsulta = horaIniConsulta;
        this.fechaIniConsulta = fechaIniConsulta;
        this.horaFinConsulta = horaFinConsulta;
        this.fechaFinConsulta = fechaFinConsulta;
        this.idPaciente = idPaciente;
        this.idUrgTriage = idUrgTriage;
        this.idPrestador = idPrestador;
        this.enfermedadActual = enfermedadActual;
        this.tipoCita = tipoCita;
        this.idServicio = idServicio;
        this.idUrgDetalleConsulta = idUrgDetalleConsulta;
        this.urgPrescripcionMedicamentolist = urgPrescripcionMedicamentolist;
        this.urgOrdenCobro = urgOrdenCobro;
        this.urgNotasMedicasList = urgNotasMedicasList;
        this.urgNotasEnfermeriaList = urgNotasEnfermeriaList;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Boolean getAtendida() {
        return atendida;
    }

    public void setAtendida(Boolean atendida) {
        this.atendida = atendida;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    public String getEnfermedadActual() {
        return enfermedadActual;
    }

    public void setEnfermedadActual(String enfermedadActual) {
        this.enfermedadActual = enfermedadActual;
    }

    public CfgClasificaciones getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(CfgClasificaciones tipoCita) {
        this.tipoCita = tipoCita;
    }

    public FacServicio getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(FacServicio idServicio) {
        this.idServicio = idServicio;
    }

    public UrgTriage getIdUrgTriage() {
        return idUrgTriage;
    }

    public void setIdUrgTriage(UrgTriage idUrgTriage) {
        this.idUrgTriage = idUrgTriage;
    }

    public UrgDetalleConsulta getIdUrgDetalleConsulta() {
        return idUrgDetalleConsulta;
    }

    public void setIdUrgDetalleConsulta(UrgDetalleConsulta idUrgDetalleConsulta) {
        this.idUrgDetalleConsulta = idUrgDetalleConsulta;
    }

    @XmlTransient
    public List<UrgPrescripcionMedicamento> getUrgPrescripcionMedicamentolist() {
        return urgPrescripcionMedicamentolist;
    }

    public void setUrgPrescripcionMedicamentolist(List<UrgPrescripcionMedicamento> urgPrescripcionMedicamentolist) {
        this.urgPrescripcionMedicamentolist = urgPrescripcionMedicamentolist;
    }

    public UrgOrdenCobro getUrgOrdenCobro() {
        return urgOrdenCobro;
    }

    public void setUrgOrdenCobro(UrgOrdenCobro urgOrdenCobro) {
        this.urgOrdenCobro = urgOrdenCobro;
    }

    @XmlTransient
    public List<UrgNotasMedicas> getUrgNotasMedicasList() {
        return urgNotasMedicasList;
    }

    public void setUrgNotasMedicasList(List<UrgNotasMedicas> urgNotasMedicasList) {
        this.urgNotasMedicasList = urgNotasMedicasList;
    }

    @XmlTransient
    public List<UrgNotasEnfermerias> getUrgNotasEnfermeriaList() {
        return urgNotasEnfermeriaList;
    }

    public void setUrgNotasEnfermeriaList(List<UrgNotasEnfermerias> urgNotasEnfermeriaList) {
        this.urgNotasEnfermeriaList = urgNotasEnfermeriaList;
    }

    public UrgPlanManejoPaciente getUrgDestinoPaciente() {
        return urgDestinoPaciente;
    }

    public void setUrgDestinoPaciente(UrgPlanManejoPaciente urgDestinoPaciente) {
        this.urgDestinoPaciente = urgDestinoPaciente;
    }

    public CfgCama getCfgCama() {
        return cfgCama;
    }

    public void setCfgCama(CfgCama cfgCama) {
        this.cfgCama = cfgCama;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.idConsulta);
        hash = 17 * hash + Objects.hashCode(this.idPaciente);
        hash = 17 * hash + Objects.hashCode(this.idUrgTriage);
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
        final UrgConsultaPacienteUrgencia other = (UrgConsultaPacienteUrgencia) obj;
        if (!Objects.equals(this.idConsulta, other.idConsulta)) {
            return false;
        }
        if (!Objects.equals(this.idUrgTriage, other.idUrgTriage)) {
            return false;
        }
        if (!Objects.equals(this.idPrestador, other.idPrestador)) {
            return false;
        }
        return true;
    }

}
