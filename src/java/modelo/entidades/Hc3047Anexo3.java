/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sofimar
 */
@Entity
@Table(name = "hc_3047_anexo3")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hc3047Anexo3.findAll", query = "SELECT h FROM Hc3047Anexo3 h"),
    @NamedQuery(name = "Hc3047Anexo3.findById3047anexo3", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.id3047anexo3 = :id3047anexo3"),
    @NamedQuery(name = "Hc3047Anexo3.findByNumerosolicitud", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.numerosolicitud = :numerosolicitud"),
    @NamedQuery(name = "Hc3047Anexo3.findByFechadocumento", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.fechadocumento = :fechadocumento"),
    @NamedQuery(name = "Hc3047Anexo3.findByAccidentetransito", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.accidentetransito = :accidentetransito"),
    @NamedQuery(name = "Hc3047Anexo3.findByIdtiposerviciosolicitado", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.idtiposerviciosolicitado = :idtiposerviciosolicitado"),
    @NamedQuery(name = "Hc3047Anexo3.findByIdprioridadservicio", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.idprioridadservicio = :idprioridadservicio"),
    @NamedQuery(name = "Hc3047Anexo3.findByIdubicacionpaciente", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.idubicacionpaciente = :idubicacionpaciente"),
    @NamedQuery(name = "Hc3047Anexo3.findByIdcama", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.idcama = :idcama"),
    @NamedQuery(name = "Hc3047Anexo3.findByManejoguia", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.manejoguia = :manejoguia"),
    @NamedQuery(name = "Hc3047Anexo3.findByJustificacion", query = "SELECT h FROM Hc3047Anexo3 h WHERE h.justificacion = :justificacion")})
public class Hc3047Anexo3 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_3047anexo3")
    private Integer id3047anexo3;
    @Size(max = 10)
    @Column(name = "numerosolicitud")
    private String numerosolicitud;
    @Column(name = "fechadocumento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadocumento;
    @Column(name = "accidentetransito")
    private Boolean accidentetransito;
    @Column(name = "idtiposerviciosolicitado")
    private Integer idtiposerviciosolicitado;
    @Column(name = "idprioridadservicio")
    private Integer idprioridadservicio;
    @Column(name = "idubicacionpaciente")
    private Integer idubicacionpaciente;
    @Size(max = 10)
    @Column(name = "idcama")
    private String idcama;
    @Size(max = 150)
    @Column(name = "manejoguia")
    private String manejoguia;
    @Size(max = 255)
    @Column(name = "justificacion")
    private String justificacion;
    @OneToMany(mappedBy = "id3047anexo3")
    private Collection<Hc3047Anexo31> hc3047Anexo31Collection;
    @JoinColumn(name = "idservicio", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones idservicio;
    @JoinColumn(name = "idorigenatencion", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones idorigenatencion;
    @JoinColumn(name = "cei10_0", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico cei100;
    @JoinColumn(name = "cei10_1", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico cei101;
    @JoinColumn(name = "cei10_2", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico cei102;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes idPaciente;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idUsuario;

    public Hc3047Anexo3() {
    }

    public Hc3047Anexo3(Integer id3047anexo3) {
        this.id3047anexo3 = id3047anexo3;
    }

    public Integer getId3047anexo3() {
        return id3047anexo3;
    }

    public void setId3047anexo3(Integer id3047anexo3) {
        this.id3047anexo3 = id3047anexo3;
    }

    public String getNumerosolicitud() {
        return numerosolicitud;
    }

    public void setNumerosolicitud(String numerosolicitud) {
        this.numerosolicitud = numerosolicitud;
    }

    public Date getFechadocumento() {
        return fechadocumento;
    }

    public void setFechadocumento(Date fechadocumento) {
        this.fechadocumento = fechadocumento;
    }

    public Boolean getAccidentetransito() {
        return accidentetransito;
    }

    public void setAccidentetransito(Boolean accidentetransito) {
        this.accidentetransito = accidentetransito;
    }

    public Integer getIdtiposerviciosolicitado() {
        return idtiposerviciosolicitado;
    }

    public void setIdtiposerviciosolicitado(Integer idtiposerviciosolicitado) {
        this.idtiposerviciosolicitado = idtiposerviciosolicitado;
    }

    public Integer getIdprioridadservicio() {
        return idprioridadservicio;
    }

    public void setIdprioridadservicio(Integer idprioridadservicio) {
        this.idprioridadservicio = idprioridadservicio;
    }

    public Integer getIdubicacionpaciente() {
        return idubicacionpaciente;
    }

    public void setIdubicacionpaciente(Integer idubicacionpaciente) {
        this.idubicacionpaciente = idubicacionpaciente;
    }

    public String getIdcama() {
        return idcama;
    }

    public void setIdcama(String idcama) {
        this.idcama = idcama;
    }

    public String getManejoguia() {
        return manejoguia;
    }

    public void setManejoguia(String manejoguia) {
        this.manejoguia = manejoguia;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    @XmlTransient
    public Collection<Hc3047Anexo31> getHc3047Anexo31Collection() {
        return hc3047Anexo31Collection;
    }

    public void setHc3047Anexo31Collection(Collection<Hc3047Anexo31> hc3047Anexo31Collection) {
        this.hc3047Anexo31Collection = hc3047Anexo31Collection;
    }

    public CfgClasificaciones getIdservicio() {
        return idservicio;
    }

    public void setIdservicio(CfgClasificaciones idservicio) {
        this.idservicio = idservicio;
    }

    public CfgClasificaciones getIdorigenatencion() {
        return idorigenatencion;
    }

    public void setIdorigenatencion(CfgClasificaciones idorigenatencion) {
        this.idorigenatencion = idorigenatencion;
    }

    public CfgDiagnostico getCei100() {
        return cei100;
    }

    public void setCei100(CfgDiagnostico cei100) {
        this.cei100 = cei100;
    }

    public CfgDiagnostico getCei101() {
        return cei101;
    }

    public void setCei101(CfgDiagnostico cei101) {
        this.cei101 = cei101;
    }

    public CfgDiagnostico getCei102() {
        return cei102;
    }

    public void setCei102(CfgDiagnostico cei102) {
        this.cei102 = cei102;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public CfgUsuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(CfgUsuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id3047anexo3 != null ? id3047anexo3.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hc3047Anexo3)) {
            return false;
        }
        Hc3047Anexo3 other = (Hc3047Anexo3) object;
        if ((this.id3047anexo3 == null && other.id3047anexo3 != null) || (this.id3047anexo3 != null && !this.id3047anexo3.equals(other.id3047anexo3))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Hc3047Anexo3[ id3047anexo3=" + id3047anexo3 + " ]";
    }
    
}
