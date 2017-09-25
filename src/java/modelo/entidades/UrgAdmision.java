/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import beans.enumeradores.EstadoAdmisionPaciente;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
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
@Table(name = "urg_admision", schema = "public")
@NamedQueries({
    @NamedQuery(name = "UrgAdmision.numeroAdmisionesDiarias", query = "SELECT COUNT(u) FROM UrgAdmision u WHERE u.fechaAdmision=:fecha")
    ,
    @NamedQuery(name = "UrgAdmision.findAllAdmisionesVigentes", query = "SELECT u FROM UrgAdmision u WHERE u.estado=:estadoPaciente")})

public class UrgAdmision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_admision", nullable = false)
    private Integer idAdmision;
    @Column(name = "atendida")
    private Boolean atendida;
    @Column(name = "nro_admision")
    private String nroAdmision;
    @Column(name = "hora_admision")
    @Temporal(TemporalType.TIME)
    private Date horaAdmision;
    @Column(name = "fecha_admision")
    @Temporal(TemporalType.DATE)
    private Date fechaAdmision;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente", nullable = false)
    @ManyToOne(optional = false)
    private CfgPacientes idPaciente;
    @OneToOne(mappedBy = "idAdmision")
    private UrgTriage IdUrgTriage;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoAdmisionPaciente estado;
    @JoinColumn(name = "idPrestador")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @Column(name = "hora_salida")
    @Temporal(TemporalType.TIME)
    private Date horaSalidaUrgencia;
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalidaUrgencia;

    public UrgAdmision() {
    }

    public Integer getIdAdmision() {
        return idAdmision;
    }

    public void setIdAdmision(Integer idAdmision) {
        this.idAdmision = idAdmision;
    }

    public Boolean getAtendida() {
        return atendida;
    }

    public void setAtendida(Boolean atendida) {
        this.atendida = atendida;
    }

    public String getNroAdmision() {
        return nroAdmision;
    }

    public void setNroAdmision(String nroAdmision) {
        this.nroAdmision = nroAdmision;
    }

    public Date getHoraAdmision() {
        return horaAdmision;
    }

    public void setHoraAdmision(Date horaAdmision) {
        this.horaAdmision = horaAdmision;
    }

    public Date getFechaAdmision() {
        return fechaAdmision;
    }

    public void setFechaAdmision(Date fechaAdmision) {
        this.fechaAdmision = fechaAdmision;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public UrgTriage getIdUrgTriage() {
        return IdUrgTriage;
    }

    public void setIdUrgTriage(UrgTriage IdUrgTriage) {
        this.IdUrgTriage = IdUrgTriage;
    }

    public EstadoAdmisionPaciente getEstadoAdmisionPaciente() {
        return estado;
    }

    public void setEstadoAdmisionPaciente(EstadoAdmisionPaciente estado) {
        this.estado = estado;
    }

    public EstadoAdmisionPaciente getEstado() {
        return estado;
    }

    public void setEstado(EstadoAdmisionPaciente estado) {
        this.estado = estado;
    }

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    public Date getHoraSalidaUrgencia() {
        return horaSalidaUrgencia;
    }

    public void setHoraSalidaUrgencia(Date horaSalidaUrgencia) {
        this.horaSalidaUrgencia = horaSalidaUrgencia;
    }

    public Date getFechaSalidaUrgencia() {
        return fechaSalidaUrgencia;
    }

    public void setFechaSalidaUrgencia(Date fechaSalidaUrgencia) {
        this.fechaSalidaUrgencia = fechaSalidaUrgencia;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idAdmision);
        hash = 97 * hash + Objects.hashCode(this.atendida);
        hash = 97 * hash + Objects.hashCode(this.idPaciente);
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
        final UrgAdmision other = (UrgAdmision) obj;
        if (!Objects.equals(this.idAdmision, other.idAdmision)) {
            return false;
        }
        if (!Objects.equals(this.atendida, other.atendida)) {
            return false;
        }
        if (!Objects.equals(this.idPaciente, other.idPaciente)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UrgAdmision{" + "idAdmision=" + idAdmision + ", idPaciente=" + idPaciente + '}';
    }

}
