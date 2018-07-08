/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sofimar
 */
@Entity
@Table(name = "hc_3047_anexo1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hc3047Anexo1.findAll", query = "SELECT h FROM Hc3047Anexo1 h"),
    @NamedQuery(name = "Hc3047Anexo1.findById3047anexo1", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.id3047anexo1 = :id3047anexo1"),
    @NamedQuery(name = "Hc3047Anexo1.findByNumeroinforme", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.numeroinforme = :numeroinforme"),
    @NamedQuery(name = "Hc3047Anexo1.findByFechadocumento", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.fechadocumento = :fechadocumento"),
    @NamedQuery(name = "Hc3047Anexo1.findByHoradocumento", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.horadocumento = :horadocumento"),
    @NamedQuery(name = "Hc3047Anexo1.findByTipodocumento", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.tipodocumento = :tipodocumento"),
    @NamedQuery(name = "Hc3047Anexo1.findByTipocobertura", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.tipocobertura = :tipocobertura"),
    @NamedQuery(name = "Hc3047Anexo1.findByPrimerapellido", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.primerapellido = :primerapellido"),
    @NamedQuery(name = "Hc3047Anexo1.findBySegundoapellido", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.segundoapellido = :segundoapellido"),
    @NamedQuery(name = "Hc3047Anexo1.findByPrimernombre", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.primernombre = :primernombre"),
    @NamedQuery(name = "Hc3047Anexo1.findBySegundonombre", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.segundonombre = :segundonombre"),
    @NamedQuery(name = "Hc3047Anexo1.findByNumeroidentificacion", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.numeroidentificacion = :numeroidentificacion"),
    @NamedQuery(name = "Hc3047Anexo1.findByFechanacimiento", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.fechanacimiento = :fechanacimiento"),
    @NamedQuery(name = "Hc3047Anexo1.findByObservaciones", query = "SELECT h FROM Hc3047Anexo1 h WHERE h.observaciones = :observaciones")})
public class Hc3047Anexo1 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_3047anexo1")
    private Integer id3047anexo1;
    @Size(max = 10)
    @Column(name = "numeroinforme")
    private String numeroinforme;
    @Column(name = "fechadocumento")
    @Temporal(TemporalType.DATE)
    private Date fechadocumento;
    @Size(max = 8)
    @Column(name = "horadocumento")
    private String horadocumento;
    @Column(name = "tipodocumento")
    private Integer tipodocumento;
    @Column(name = "tipocobertura")
    private Integer tipocobertura;
    @Size(max = 30)
    @Column(name = "primerapellido")
    private String primerapellido;
    @Size(max = 30)
    @Column(name = "segundoapellido")
    private String segundoapellido;
    @Size(max = 30)
    @Column(name = "primernombre")
    private String primernombre;
    @Size(max = 30)
    @Column(name = "segundonombre")
    private String segundonombre;
    @Size(max = 50)
    @Column(name = "numeroidentificacion")
    private String numeroidentificacion;
    @Column(name = "fechanacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechanacimiento;
    @Size(max = 255)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "tipoinconsistencia", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoinconsistencia;
    @JoinColumn(name = "tipo_identificacion", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoIdentificacion;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes idPaciente;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idUsuario;

    public Hc3047Anexo1() {
    }

    public Hc3047Anexo1(Integer id3047anexo1) {
        this.id3047anexo1 = id3047anexo1;
    }

    public Integer getId3047anexo1() {
        return id3047anexo1;
    }

    public void setId3047anexo1(Integer id3047anexo1) {
        this.id3047anexo1 = id3047anexo1;
    }

    public String getNumeroinforme() {
        return numeroinforme;
    }

    public void setNumeroinforme(String numeroinforme) {
        this.numeroinforme = numeroinforme;
    }

    public Date getFechadocumento() {
        return fechadocumento;
    }

    public void setFechadocumento(Date fechadocumento) {
        this.fechadocumento = fechadocumento;
    }

    public String getHoradocumento() {
        return horadocumento;
    }

    public void setHoradocumento(String horadocumento) {
        this.horadocumento = horadocumento;
    }

    public Integer getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(Integer tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public Integer getTipocobertura() {
        return tipocobertura;
    }

    public void setTipocobertura(Integer tipocobertura) {
        this.tipocobertura = tipocobertura;
    }

    public String getPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(String primerapellido) {
        this.primerapellido = primerapellido;
    }

    public String getSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(String segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public String getPrimernombre() {
        return primernombre;
    }

    public void setPrimernombre(String primernombre) {
        this.primernombre = primernombre;
    }

    public String getSegundonombre() {
        return segundonombre;
    }

    public void setSegundonombre(String segundonombre) {
        this.segundonombre = segundonombre;
    }

    public String getNumeroidentificacion() {
        return numeroidentificacion;
    }

    public void setNumeroidentificacion(String numeroidentificacion) {
        this.numeroidentificacion = numeroidentificacion;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public CfgClasificaciones getTipoinconsistencia() {
        return tipoinconsistencia;
    }

    public void setTipoinconsistencia(CfgClasificaciones tipoinconsistencia) {
        this.tipoinconsistencia = tipoinconsistencia;
    }

    public CfgClasificaciones getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(CfgClasificaciones tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
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
        hash += (id3047anexo1 != null ? id3047anexo1.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hc3047Anexo1)) {
            return false;
        }
        Hc3047Anexo1 other = (Hc3047Anexo1) object;
        if ((this.id3047anexo1 == null && other.id3047anexo1 != null) || (this.id3047anexo1 != null && !this.id3047anexo1.equals(other.id3047anexo1))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Hc3047Anexo1[ id3047anexo1=" + id3047anexo1 + " ]";
    }
    
}
