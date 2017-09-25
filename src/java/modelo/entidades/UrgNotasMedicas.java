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
@Table(name = "urg_notas_medicas", schema = "public")

public class UrgNotasMedicas implements Serializable {

    private static final long serialVersionUID = -45454543222217L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_nota", nullable = false)
    private Integer idNota;
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
    @Column(name = "nota")
    private String nota;

    public UrgNotasMedicas(Integer idNota) {
        this.idNota = idNota;
    }

    public UrgNotasMedicas() {
    }

    public UrgNotasMedicas(Integer idNota, Date hora, Date fecha, UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia, CfgUsuarios idPrestador, String nota) {
        this.idNota = idNota;
        this.hora = hora;
        this.fecha = fecha;
        this.idUrgConsultaPacienteUrgencia = idUrgConsultaPacienteUrgencia;
        this.idPrestador = idPrestador;
        this.nota = nota;
    }

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
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

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.idNota);
        hash = 17 * hash + Objects.hashCode(this.idUrgConsultaPacienteUrgencia);
        hash = 17 * hash + Objects.hashCode(this.idPrestador);
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
        final UrgNotasMedicas other = (UrgNotasMedicas) obj;
        if (!Objects.equals(this.idNota, other.idNota)) {
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
